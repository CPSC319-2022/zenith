import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchPosts } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { Button } from 'react-bootstrap';
import '../styles/Home.css';
import Pagination from './Pagination';

const Home = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [postsPerPage] = useState(4);

  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.posts);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);

  useEffect(() => {
    if (status === 'idle') {
      dispatch(fetchPosts({ postIDStart: 50, count: 50, reverse: true }));
    }
  }, [status, dispatch]);

  const lastPostIndex = currentPage * postsPerPage;
  const firstPostIndex = lastPostIndex - postsPerPage;
  const currentPosts = posts.slice(firstPostIndex, lastPostIndex);

  const navigate = useNavigate();

  function handleClick(post) {
   // console.log('post:', post);
    const postID = post.post.postID;
    console.log('postID:', postID);
    //pass the entire post object to the single post page and use it to display the post
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
      {console.log(currentPosts)}

        {currentPosts.map((post) => (
          <div className="post" key={post.postID}>
        
            <div className="img">
              <img src={post.img} alt="" />
            </div>

            <div className="content">
              <Link className="link" to={`/single-post/${post.postID}`}>
                <h1 className="post-title">{post.title}</h1>
                {/* {console.log(post.title)} */}
              </Link>
              {console.log("this:",post.content.substring(0, 50))}
              <div className="post-content"><div dangerouslySetInnerHTML={{ __html: post.content.substring(0,50)+"..." }}></div></div>

              <Button className="read-button" onClick={() => handleClick({post})}>
                Read More
              </Button>
            </div>
          </div>
        ))}
      </div>

      <Pagination
        totalPosts={posts.length}
        postsPerPage={postsPerPage}
        setCurrentPage={setCurrentPage}
        currentPage={currentPage}
      />
    </div>
  );
};

export default Home;
