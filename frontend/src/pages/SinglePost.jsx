import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch, shallowEqual } from 'react-redux';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import { fetchComments, upvoteComment, downvoteComment } from '../redux/slices/commentSlice';
import { fetchPost, upvotePost, downvotePost } from '../redux/slices/postSlice';
import { commentSliceActions } from '../redux/slices/commentSlice';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Comment from '../components/Comment';
import Filter from 'bad-words';
import { AiFillLike, AiFillDislike } from 'react-icons/ai';

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
  const [updateComments, setUpdateComments] = useState(false);

  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
  }, [id, dispatch]);

  useEffect(() => {
    dispatch(fetchComments({ postID: id, commentIDStart: 0, count: 10, reverse: false }));
  }, [id, dispatch, updateComments]);

  const checkProfanity = (text) => {
    const filter = new Filter();
    return filter.isProfane(text);
  };

  const handleUpvotePost = async (postID) => {
    await dispatch(upvotePost(postID ));
    dispatch(fetchPost({ postID: id }));
  };
  
  const handleDownvotePost = async (postID) => {
    await dispatch(downvotePost(postID));
    dispatch(fetchPost({ postID: id }));
  };

  const handleUpvoteComment = async (postID, commentID) => {
    await dispatch(upvoteComment({ postID, commentID }));
    setUpdateComments(!updateComments);
  };
  
  const handleDownvoteComment = async (postID, commentID) => {
    await dispatch(downvoteComment({ postID, commentID }));
    setUpdateComments(!updateComments);
  };


  if (status === 'loading') {
    return <div>Loading...</div>;
  }

  if (status === 'failed') {
    return <div>Error: {error}</div>;
  }

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (checkProfanity(commentBody)) {
      setProfanityError('Your comment contains profanity. Please remove it before submitting.');
      return;
    }

    const authorID = 1;

    await dispatch(
      commentSliceActions.createComment({
        postID: id,
        authorID,
        content: commentBody,
      })
    );

    setCommentBody('');

    dispatch(fetchComments({ postID:id, commentIDStart: 0, count: 10, reverse: false }));
    
  };
  

  return (
    <Container>
      <Row>
        <Col xs={12} md={10} lg={8} className="mx-auto">
          <div className="single-post">
            {/* Render post title and content */}
            {post && (
              <>
                <h1>{post.title}</h1>
                <div
                  className="post-content"
                  dangerouslySetInnerHTML={{ __html: post.content }}
                ></div>
              </>
            )}

            {/* Add upvote and downvote buttons for the post */}
            {post && (
              <div className="post-votes">
                <Button variant="outline-primary" onClick={() => handleUpvotePost(post.postID)}>
                  <AiFillLike /> {post.upvotes}
                </Button>
                <Button variant="outline-danger" onClick={() => handleDownvotePost(post.postID)}>
                  <AiFillDislike /> {post.downvotes}
                </Button>
              </div>
            )}

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
              comments.map((comment) => (
                <Comment
                  key={comment.id}
                  comment={comment}
                  onUpvote={() => handleUpvoteComment(comment.postID, comment.commentID)}
                  onDownvote={() => handleDownvoteComment(comment.postID, comment.commentID)}
                />
              ))}
            {console.log('Comments count:', comments.length)}
{console.log('Comments:', JSON.stringify(comments, null, 2))}

          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default SinglePost;

