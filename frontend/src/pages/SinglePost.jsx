import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { fetchComments } from '../redux/slices/commentSlice';
import { fetchPost } from '../redux/slices/postSlice';
import { commentSliceActions } from '../redux/slices/commentSlice';

import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Button from 'react-bootstrap/Button';

const SinglePost = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const post = useSelector((state) => state.posts.post);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);
  const comments = useSelector((state) => state.comments.comments);
  const commentsStatus = useSelector((state) => state.comments.status);
  const commentsError = useSelector((state) => state.comments.error);
  const [commentBody, setCommentBody] = useState('');

  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
    dispatch(fetchComments({ postID: id , commentIDStart: 0, count: 10, reverse:false}));
  }, [id, dispatch]);

  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }

  //Comment Box
  const handleCommentSubmit = (e) => {
    e.preventDefault();

    // Replace this line with the authorID of the currently logged-in user
    const authorID = 1;

    // Call your API to create the comment
    // You may need to create an action similar to `postSliceActions.createPost` in your postSlice
    dispatch(
      commentSliceActions.createComment({
        postID: id,
        authorID,
        content: commentBody,
      })
    );

    // Clear the comment box
    setCommentBody('');
  };

  return (
    <div className="single-post">
      {post && (
        <>
          <h1 className="post-title">{post.title}</h1>
          <div className="post-content">
            <div dangerouslySetInnerHTML={{ __html: post.content }}></div>
          </div>

          <div className="comment-box">
            <h3>Leave a comment:</h3>
            <ReactQuill
              className="editor"
              theme="snow"
              value={commentBody}
              onChange={(content) => setCommentBody(content)}
            />
            <Button onClick={handleCommentSubmit} variant="primary">
              Submit Comment
            </Button>
          </div>
          {commentsStatus === 'loading' && <div>Loading comments...</div>}
          {commentsStatus === 'failed' && <div>Error: {commentsError}</div>}
          {commentsStatus === 'succeeded' &&
            comments.map((comment) => (
              <div key={comment.id} className="comment">
                <div className="comment-author">{comment.authorName}</div>
                <div
                  className="comment-content"
                  dangerouslySetInnerHTML={{ __html: comment.content }}
                ></div>
              </div>
            ))}
        </>
      )}
    </div>
  );
};

export default SinglePost;
