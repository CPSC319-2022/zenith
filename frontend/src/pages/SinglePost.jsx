import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch, shallowEqual } from 'react-redux';
import { Container, Row, Col, Button, ButtonGroup } from 'react-bootstrap';
import { useParams } from 'react-router-dom';

import { fetchComments, upvoteComment, downvoteComment, editComment, deleteComment } from '../redux/slices/commentSlice';
import { fetchPost, upvotePost, downvotePost, editPost, deletePost } from '../redux/slices/postSlice';
import { commentSliceActions } from '../redux/slices/commentSlice';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Comment from '../components/Comment';
import '../styles/SinglePost.css';
import Filter from 'bad-words';
import { AiFillLike, AiFillDislike, AiOutlineEdit, AiOutlineDelete } from 'react-icons/ai';


const SinglePost = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const post = useSelector((state) => state.posts.post);
  const status = useSelector((state) => state.posts.status);
  const error = useSelector((state) => state.posts.error);
  // const currentUser = useSelector((state) => state.users.currentUser);
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);


  const comments = useSelector((state) => state.comments.comments, shallowEqual);
  const commentsStatus = useSelector((state) => state.comments.status);
  const commentsError = useSelector((state) => state.comments.error);
  const [authorUsername, setAuthorUsername] = useState('');
  const [authorPicture, setAuthorPicture] = useState('');
  const [currentUser, setCurrentUser] = useState(null);
  const [commentBody, setCommentBody] = useState('');
  const [profanityError, setProfanityError] = useState('');
  const [updateComments, setUpdateComments] = useState(false);
  const [postDeleted, setPostDeleted] = useState(false);
  const [editingPost, setEditingPost] = useState(false);
  const [editedPostContent, setEditedPostContent] = useState('');
  const [postUpdated, setPostUpdated] = useState(false);
  const [postVoteErrorMessage, setPostVoteErrorMessage] = useState('');
  const [commentVoteErrorMessage, setCommentVoteErrorMessage] = useState('');
  const [showPostVoteErrorMessage, setShowPostVoteErrorMessage] = useState(false);
  const [showCommentVoteErrorMessage, setShowCommentVoteErrorMessage] = useState(false);




  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
  }, [id, dispatch, postUpdated]);


  useEffect(() => {
    dispatch(fetchPost({ postID: id }));
  }, [id, dispatch]);

  useEffect(() => {
    dispatch(fetchComments({ postID: id, commentIDStart: 0, count: 10, reverse: false }))
      .unwrap()
      .then((fetchedComments) => {
        fetchedComments.forEach((comment) => {
          dispatch(userSliceActions.fetchUser(comment.authorID))
            .then((response) => {
              if (response.meta.requestStatus === 'fulfilled') {
                console.log('picture:', response.payload.profilePicture);
                console.log('author:', response.payload.username);
                comment.authorUsername = response.payload.username;
                comment.authorProfilePicture = response.payload.profilePicture;
              }
            });
        });
      });
  }, [id, dispatch, updateComments]);

  useEffect(() => {
    if (isAuthenticated) {
      dispatch(fetchCurrentUserByToken()).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          setCurrentUser(response.payload);
        }
      });
    }
  }, [isAuthenticated, dispatch]);

  useEffect(() => {
    if (postVoteErrorMessage) {
      setShowPostVoteErrorMessage(true);
      setTimeout(() => {
        setShowPostVoteErrorMessage(false);
      }, 2000);
    }
    if (commentVoteErrorMessage) {
      setShowCommentVoteErrorMessage(true);
      setTimeout(() => {
        setShowCommentVoteErrorMessage(false);
      }, 2000);
    }
  }, [postVoteErrorMessage,commentVoteErrorMessage]);
  

  const checkProfanity = (text) => {
    const filter = new Filter();
    return filter.isProfane(text);
  };

  useEffect(() => {
    if (post) {
      dispatch(userSliceActions.fetchUser(post.authorID))
        .then((response) => {
          if (response.meta.requestStatus === 'fulfilled') {
            setAuthorUsername(response.payload.username);
            setAuthorPicture(response.payload.profilePicture);
          }
        });
    }
  }, [post, dispatch]);


  const handleUpvotePost = async (postID) => {
    setPostVoteErrorMessage('');
    const result = await dispatch(upvotePost(postID));
    if (result.error) {
      console.log('error: ', result.error);
      setPostVoteErrorMessage('Already upvoted');
    } else {
      dispatch(fetchPost({ postID: id }));
    }

  };

  const handleDownvotePost = async (postID) => {
    setPostVoteErrorMessage('');
    const result = await dispatch(downvotePost(postID));
    dispatch(fetchPost({ postID: id }));
    if (result.error) {
      console.log('error: ', result.error);
      setPostVoteErrorMessage('Already downvoted');
    } else {
      dispatch(fetchPost({ postID: id }));
    }
  };



  const handleEditPost = () => {
    setEditingPost(true);
    setEditedPostContent(post.content);
  };

  const handleDeletePost = async (postID) => {
    await dispatch(deletePost(postID));
    setPostDeleted(true);
    postVoteErrorMessage('');
  };


  const handleSave = async (postID, title, content, allowComments) => {
    if (editingPost) {
      content = editedPostContent.toString();
      await dispatch(editPost({ postID, title, content, allowComments }));
      setEditingPost(false);
      setPostUpdated(!postUpdated); // This line will trigger the refetch
    }
  };

  const handleCancel = () => {
    setEditingPost(false);
  };





  const handleUpvoteComment = async (postID, commentID) => {
    setCommentVoteErrorMessage('');
    const result = await dispatch(upvoteComment({ postID, commentID }));
    if (result.error) {
      setCommentVoteErrorMessage('Already upvoted');
    } else {
      setUpdateComments(!updateComments);
    }
  };

  const handleDownvoteComment = async (postID, commentID) => {
    setCommentVoteErrorMessage('');
    const result = await dispatch(downvoteComment({ postID, commentID }));
    if (result.error) {
      setCommentVoteErrorMessage('Already downvoted');
    } else {

      setUpdateComments(!updateComments);
    }
  };

  const handleDeleteComment = async (postID, commentID) => {
    await dispatch(deleteComment({ postID, commentID }));
    setUpdateComments(!updateComments);
    commentVoteErrorMessage('');
  };

  const handleEditComment = async (postID, commentID, content, editedContent) => {
    content = editedContent.toString();
    await dispatch(editComment({ postID, commentID, content }));
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


    await dispatch(
      commentSliceActions.createComment({
        postID: id,
        content: commentBody,
      })
    );

    setCommentBody('');

    dispatch(fetchComments({ postID: id, commentIDStart: 0, count: 10, reverse: false }));

  };
  if (isAuthenticated && currentUser === null) {
    return <div>Loading user data...</div>; // or render a login prompt
  }
  return (
    <>
      {postDeleted && (window.location.href = '/')}
      {!postDeleted && (
        <Container>
          <Row>
            <Col xs={12} md={10} lg={8} className="mx-auto">
              <div className="single-post">
                {/* Render post title, author username, creation date, and content */}
                {post && (
                  <>
                    
                    {/* <img src={authorPicture} alt="Profile Picture" /> <p>{authorUsername} | {new Date(post.creationDate).toLocaleString().split(',')[0]}</p> */}
                    <div className="author-info">
                      <Link to={`/profile/${post.authorID}`}>
                        <img className="author-picture" src={authorPicture} alt={authorUsername} referrerpolicy="no-referrer" />
                        <h6>By: {authorUsername}</h6>
                      </Link>
                    </div>
                    <h1>{post.title}</h1>
                    <p>Published: {new Date(post.creationDate).toLocaleDateString()}</p>
                    <div className="post-thumbnail-pic">
                      <img src={post.thumbnailURL} alt="Post Thumbnail" referrerpolicy="no-referrer"/>
                    </div>
                    {editingPost ? (
                      <ReactQuill
                        className="editor"
                        theme="snow"
                        value={editedPostContent}
                        onChange={(content) => setEditedPostContent(content)}
                      />
                    ) : (
                      <div
                        className="post-content-complete"
                        dangerouslySetInnerHTML={{ __html: post.content }}
                      ></div>
                    )}
                  </>
                )}
                {/* Add upvote, downvote, edit, and delete buttons for the post */}
                {post && (
                  <div className="post-votes">
                     
                    {isAuthenticated && (
                      <Button
                        variant="outline-primary"
                        className='post-actions'
                        onClick={() => handleUpvotePost(post.postID)}
                      >
                        <AiFillLike/> {post.upvotes}
                      </Button>
                    )}
                    {isAuthenticated && (
                      <Button
                        variant="outline-danger"
                        className='post-actions'
                        onClick={() => handleDownvotePost(post.postID)}
                      >
                        <AiFillDislike /> {post.downvotes}
                      </Button>
                    )}
                    {!isAuthenticated && (
                      <Button
                        variant="outline-primary"
                        className='post-actions'
                        disabled
                      >
                        <AiFillLike/> {post.upvotes}
                      </Button>
                    )}
                    {!isAuthenticated && (
                      <Button
                        variant="outline-danger"
                        className='post-actions'
                        disabled
                      >
                        <AiFillDislike /> {post.downvotes}
                      </Button>
                    )}
                    {isAuthenticated && (
                      editingPost ? (
                        <>
                          <Button variant="success"  className='post-actions' onClick={() => handleSave(post.postID, post.title, post.content, post.allowComments)}>
                            Save
                          </Button>
                          <Button variant="secondary" className='post-actions' onClick={handleCancel}>
                            Cancel
                          </Button>
                        </>
                      ) :
                        ((currentUser.userID === post.authorID) &&
                          <Button variant="outline-info" className='post-actions' onClick={handleEditPost}>
                            <AiOutlineEdit />
                          </Button>
                        )
                    
                    )
                   
                    }
                

                    {isAuthenticated && (
                      ((currentUser !== (null || undefined)) && (currentUser.userID === post.authorID) || (currentUser.userLevel === 'ADMIN')) &&
                      <Button variant="danger" className='post-actions' onClick={() => handleDeletePost(post.postID)}>
                        <AiOutlineDelete />
                      </Button>
                    )}

                    {showPostVoteErrorMessage && <div className="post-vote-error">{postVoteErrorMessage}</div>}




                    {/* comments */}
                    <div className="my-4">
                      {isAuthenticated ? (
                        <div className="comment-box">
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
                      ) : (
                        <div className="login-button-container">
                          <Link to="/Login">
                            <Button variant="primary">Login to comment</Button>
                          </Link>
                        </div>
                      )}
                    </div>
                    {showCommentVoteErrorMessage && <div className="comment-vote-error">{commentVoteErrorMessage}</div>}

                    {commentsStatus === 'loading' && <div>Loading comments...</div>}
                    {commentsStatus === 'failed' && <div>Error: {commentsError}</div>}
                    {commentsStatus === 'succeeded' &&

                      comments.map((comment) => (
                        <Comment
                          key={comment.id}
                          comment={comment}
                          userIsLoggedIn={isAuthenticated}
                          currentUser={currentUser}
                          authorUsername={comment.authorUsername}
                          authorProfilePicture={comment.authorProfilePicture}
                          creationDate={comment.creationDate}
                          userIsCommentAuthor={currentUser && currentUser.userID === comment.authorID}
                          userIsAdmin={currentUser && currentUser.userLevel === 'ADMIN'}
                          onUpvote={() => handleUpvoteComment(comment.postID, comment.commentID)}
                          onDownvote={() => handleDownvoteComment(comment.postID, comment.commentID)}
                          onEdit={(editedContent) => handleEditComment(comment.postID, comment.commentID, comment.content, editedContent)}
                          onDelete={() => handleDeleteComment(comment.postID, comment.commentID)}

                        />
                      ))}
                  </div>
                )}
              </div>
            </Col>
          </Row>
        </Container>
      )}
    </>
  );
};

export default SinglePost;