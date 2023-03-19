import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch, shallowEqual } from 'react-redux';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import { fetchComments } from '../redux/slices/commentSlice';
import { fetchPost } from '../redux/slices/postSlice';
import { commentSliceActions } from '../redux/slices/commentSlice';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Comment from '../components/Comment';
import Filter from 'bad-words';


const SinglePost = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const post = useSelector((state) => state.posts.post);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);
  const comments = useSelector((state) => state.comments.comments, shallowEqual);
  const commentsStatus = useSelector((state) => state.comments.status);
  const commentsError = useSelector((state) => state.comments.error);
  const [commentBody, setCommentBody] = useState('');
  const [profanityError, setProfanityError] = useState('');


  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
  }, [id, dispatch]);

  useEffect(() => {
    dispatch(fetchComments({ postID: id , commentIDStart: 0, count: 10, reverse:false}));
  }, [id, dispatch]);

  const checkProfanity = (text) => {
    const filter = new Filter();
    return filter.isProfane(text);
  };


  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }

  //Comment Box
  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    //check for profanity
    if (checkProfanity(commentBody)) {
      setProfanityError('Your comment contains profanity. Please remove it before submitting.');
      return;
    }

    // Replace this line with the authorID of the currently logged-in user
    const authorID = 1;

    // Call your API to create the comment
    await dispatch(
      commentSliceActions.createComment({
        postID: id,
        authorID,
        content: commentBody,
      })
    );

    // Clear the comment box
    setCommentBody('');

    // Refresh comments
    dispatch(fetchComments({ postID: id , commentIDStart: 0, count: 10, reverse:false}));
  };

  return (
    <Container>
    <Row>
      <Col xs={12} md={10} lg={8} className="mx-auto">
        <div className="single-post">
          {/* ... (rest of your JSX elements) */}
          <div className="comment-box my-4">
            <h3>Leave a comment:</h3>
            <ReactQuill
              className="editor"
              theme="snow"
              value={commentBody}
              onChange={(content) => setCommentBody(content)}
            />
            <Button onClick={handleCommentSubmit} className="mt-2" variant="primary">
              Submit Comment
            </Button>
            {profanityError && <div className="profanity-error">{profanityError}</div>}
          </div>
          {commentsStatus === 'loading' && <div>Loading comments...</div>}
          {commentsStatus === 'failed' && <div>Error: {commentsError}</div>}
          {commentsStatus === 'succeeded' &&
              comments.map((comment) => <Comment key={comment.id} comment={comment} />)}
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default SinglePost;