import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchPosts } from '../redux/slices/postSlice';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { Button, Carousel, Card } from 'react-bootstrap';
import '../styles/Home.css';
import '../styles/Carousel.css';
<<<<<<< HEAD
import Searchbar from '../components/Searchbar';
=======
import { highestPostIndex } from '../api';
>>>>>>> acab3017822d7f2e82d06df29e5c744054e5d2ff


const Home = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [highestIndex, setHighestIndex] = useState(null);
  const postsPerPage = 9;
  const { resetStatus } = postSliceActions;

  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.posts);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);

  useEffect(() => {
    const fetchCurrentPosts = () => {
      if (status === 'idle') {
        const postIDStart = (currentPage - 1) * postsPerPage;
        dispatch(fetchPosts({ postIDStart, count: postsPerPage, reverse: false }));
      }
    };

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

  

  const nextPage = () => {
    setCurrentPage(currentPage + 1);
    dispatch(resetStatus());
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
    dispatch(resetStatus());
  };

  const navigate = useNavigate();

  function handleClick(post) {
    const postID = post.post.postID;
    navigate(`/single-post/${postID}`);
  }

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }
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
      <Searchbar />
    </div>

<div className="container post-area">
        <div className="row">
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
