import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchPosts } from '../redux/slices/postSlice';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import '../styles/Home.css';

const Home = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 5;
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
  
    fetchCurrentPosts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, status, dispatch]);
  

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

  return (
    <div className="home">
      <div className="posts">
        {posts.map((post) => (
          <div className="post" key={post.postID}>
            <div className="img">
              <img src={post.img} alt="" />
            </div>

            <div className="content">
              <Link className="link" to={`/single-post/${post.postID}`}>
                <h1 className="post-title">{post.title}</h1>
              </Link>
              <div className="post-content">
                <div dangerouslySetInnerHTML={{ __html: post.content.substring(0, 50) + '...' }}></div>
              </div>

              <Button className="read-button" onClick={() => handleClick({ post })}>
                Read More
              </Button>
            </div>
          </div>
        ))}
      </div>

      <div className="pagination">
        {currentPage > 1 && (
          <Button className="prev-button" onClick={prevPage}>
            Previous
          </Button>
        )}
        <Button className="next-button" onClick={nextPage}>
          Next
        </Button>
      </div>
    </div>
  );
};

export default Home;
