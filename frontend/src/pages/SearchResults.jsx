import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { Button, Card } from 'react-bootstrap';
import '../styles/Home.css';

const Home = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [start, setStart] = useState(0);
  const [end, setEnd] = useState(9);
  const postsPerPage = 9;
  const { resetStatus } = postSliceActions;

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.searchResults);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);
  const resultCount = posts.length;

  const nextPage = () => {
    setCurrentPage(currentPage + 1);
    setStart(start + 9);
    setEnd(end + 9);
    dispatch(resetStatus());
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
    setStart(start - 9);
    setEnd(end - 9);
    dispatch(resetStatus());
  };

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

  const isLastPage = currentPage * postsPerPage >= resultCount;
  return (

  <div className="home">

      <div className="container post-area">
        <div className="row">
          {posts.map((post) => (
            <div className="col-md-4 col-sm-6 mb-4 post-cards " key={post.postID}>
              <Card style={{ width: '18rem' }}>
                <Card.Img variant="top" src={post.thumbnailURL} />
                <Card.Body>
                  <Card.Title>
                    <Link className="link post-title" to={`/single-post/${post.postID}`}>
                      {post.title}
                    </Link>
                  </Card.Title>
                  <Card.Text  className='post-content'>
                    <div dangerouslySetInnerHTML={{ __html: post.content}}></div>
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
        <div style={{textAlign: "center"}}>
          {resultCount === 0 && (
            <h1>No results found</h1>
          )}
        </div>
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
