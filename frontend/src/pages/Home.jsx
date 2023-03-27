import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchPosts } from '../redux/slices/postSlice';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { Button, Carousel, Card } from 'react-bootstrap';
import '../styles/Home.css';
import '../styles/Carousel.css';
import { highestPostIndex } from '../api';
import Form from 'react-bootstrap/Form';

const Home = () => {
  //The two states below are used to keep track of the current page of posts and the highest index of posts
  const [currentPage, setCurrentPage] = useState(1);
  const [highestIndex, setHighestIndex] = useState(null);
  const [isReversed, setIsReversed] = useState(true);
  const postsPerPage = 9;
  const { resetStatus } = postSliceActions;

  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.posts);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);

/*
1 2 3 4 5 6 7 8

2 as postsPerPage 8,7 6,5 4,3 2,1

1 2 99 100
(state.posts.posts[state.posts.posts.length-1]).post.postID

2 as postsPerPage 8,5 2,1

postsPerPage + 1 if not on page 1
get rid of head on res
*/

  //This useEffect is used to fetch the posts for the current page
  useEffect(() => {
    const fetchCurrentPosts = () => {
      if (status === 'idle') {
        //oldest post first when isReversed false
        if (isReversed === true) {
          if (currentPage == 1){
            const postIDStart = 1;
            dispatch(fetchPosts({ postIDStart, count: postsPerPage, reverse: isReversed }));
          }else {
            const postIDStart = (posts[posts.length-1]).post.postID;
            dispatch(fetchPosts({ postIDStart, count: postsPerPage+1, reverse: isReversed }));
          }
        } else {
          if (currentPage == 1) {
            const postIDStart = highestIndex;
            dispatch(fetchPosts({ postIDStart, count: postsPerPage, reverse: isReversed }));
          } else {
            const postIDStart = (posts[posts.length-1]).post.postID;
            dispatch(fetchPosts({ postIDStart, count: postsPerPage+1, reverse: isReversed }));
          }
        }
}};
//This functin fetches the highest index of posts in the db
    const fetchHighestIndex = async () => {
      try {
        const index = await highestPostIndex();
        setHighestIndex(index);
      } catch (error) {
        console.error('Failed to fetch the highest post index:', error.message);
      }
    };

    fetchCurrentPosts();
    if (highestIndex === null) {
      fetchHighestIndex();
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, status, dispatch, highestIndex]);


  
//This function is used update the current page when next button is clicked
  const nextPage = () => {
    setCurrentPage(currentPage + 1);
    dispatch(resetStatus());
  };
// This function is used to update the current page when previous button is clicked
  const prevPage = () => {
    setCurrentPage(currentPage - 1);
    dispatch(resetStatus());
  };

  const navigate = useNavigate();

  function handleClick(post) {
    const postID = post.post.postID;
    navigate(`/single-post/${postID}`);
  }

  function handleSwitch() {
    setIsReversed(!isReversed);
  }

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }
  //this is used to determine if the current page is the last page  
console.log('highestIndex:',highestIndex);
  const isLastPage = currentPage * postsPerPage >= highestIndex;
  return (


    <div className="home">
       {posts.length > 0 && (
    <Carousel>
    {posts.slice(0, 3).map((post) => (
      <Carousel.Item key={post.postID}>
        <img
          src={`https://source.unsplash.com/random/900x300?sig=${post.postID}`}
          alt={`Post ${post.postID}`}
          className="d-block w-100"
        />
        <Carousel.Caption>
          <Link to={`/single-post/${post.postID}`} className="carousel-title carousel-post-title-home">
            <h3>{post.title}</h3>
          </Link>
          <div
            className="carousel-content carousel-post-content-home"
            dangerouslySetInnerHTML={{ __html: post.content.substring(0, 50) + '...' }}
          ></div>
        </Carousel.Caption>
      </Carousel.Item>
    ))}
  </Carousel>
      )}

<div>
  <Form>
    <Form.Check 
      type="switch"
      id="custom-switch"
      label="Check this switch"
      onChange={handleSwitch}
    />
  </Form>
</div>

<div className="container post-area">
        <div className="row">
          { (currentPage != 1) ? (posts = posts.shift()):null}
          {posts.map((post) => (
            <div className="col-md-4 col-sm-6 mb-4 post-cards " key={post.postID}>
              <Card style={{ width: '18rem' }}>
                <Card.Img variant="top" src={`https://source.unsplash.com/random/300x200?sig=${post.postID}`} />
                <Card.Body>
                  <Card.Title>
                    <Link className="link" to={`/single-post/${post.postID}`}>
                      {post.title}
                    </Link>
                  </Card.Title>
                  <Card.Text>
                    <div dangerouslySetInnerHTML={{ __html: post.content.substring(0, 50) + '...' }}></div>
                  </Card.Text>
                  <Button className="read-button" onClick={() => handleClick({ post })}>
                    Read More
                  </Button>
                </Card.Body>
              </Card>
            </div>
          ))}
        </div>
      </div>


      <div className="container">
      <div className="row">
        <div className="col-12 d-flex flex-column align-items-center gap-2">
         {!isLastPage && (
          <Button className="next-button btn-lg mb-2" style={{ width: '200px' }} onClick={nextPage} disabled={isLastPage}>
            Next
          </Button>
          )}
          {currentPage > 1 && (
            <Button className="prev-button btn-lg mb-2" style={{ width: '200px' }} onClick={prevPage}>
              Previous
            </Button>
          )}
        </div>
      </div>
    </div>
  </div>
);
};

export default Home;
