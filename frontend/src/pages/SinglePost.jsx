import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { fetchPost } from '../redux/slices/postSlice';

const SinglePost = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const post = useSelector((state) => state.posts.post);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);

  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
  }, [id, dispatch]);
  
  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }

  return (
    <div className="single-post">
      {post && (
        <>
          <h1 className="post-title">{post.title}</h1>
          <p className="post-content">{post.content}</p>
        </>
      )}
    </div>
  );
};

export default SinglePost;
