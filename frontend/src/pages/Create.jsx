import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link } from 'react-router-dom';
import { promoteUser } from '../api';

const CreatePost = () => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const currentUser = useSelector((state) => state.auth.currentUser);

  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [promotionLevel, setPromotionLevel] = useState('Contributor');
  const [promotionReason, setPromotionReason] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const resetMessages = () => {
    setErrorMessage('');
    setSuccessMessage('');
  };

  const handlePromotionRequest = async (e) => {
    e.preventDefault();
    try {
      await promoteUser({
        target: promotionLevel,
        reason: promotionReason,
      });
      setSuccessMessage('Promotion request sent successfully.');
    } catch (error) {
      console.error('Error requesting promotion:', error);
      setErrorMessage('Error requesting promotion. Please try again later.');
    }
  };

  const handleCreatePost = (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      setErrorMessage('Please login to create a post.');
      return;
    }

    // Replace authorID with the clientID of the currently logged-in user
    const allowComments = true; // You can change this according to your requirements

    dispatch(
      postSliceActions.createPost({
        title,
        content: body,
        allowComments,
      })
    );

    setTitle('');
    setBody('');
  };


  if (!isAuthenticated) {
    return (
      <div className="login-button">
        <Link to="/login">
          <Button variant="primary">Login to Create a Post</Button>
        </Link>
      </div>
    );
  }

  return (
    <>
      {errorMessage && (
        <div className="alert alert-danger">
          {errorMessage}
          <Button
            className="ml-2"
            variant="outline-danger"
            onClick={() => resetMessages()}
          >
            Retry
          </Button>
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success">
          {successMessage}
        </div>
      )}
      {(!successMessage && !errorMessage) && (
        <>
          {currentUser?.userLevel !== 'contributor' && currentUser?.userLevel !== 'admin' && (
            <>
              <h1 className="create-title">Only contributors and admins can create a post.</h1>
              <Button variant="primary" onClick={() => setShowForm(!showForm)}>
                Request Level Upgrade
              </Button>
              {showForm && (
                <Form onSubmit={handlePromotionRequest}>
                  <Row className="mb-3">
                    <Form.Group as={Col} controlId="promotionLevel">
                      <Form.Label>Promotion Level</Form.Label>
                      <Form.Select
                        value={promotionLevel}
                        onChange={(e) => setPromotionLevel(e.target.value)}
                      >
                                            <option>Contributor</option>
                    <option>Admin</option>
                  </Form.Select>
                </Form.Group>
              </Row>
              <Row className="mb-3">
                <Form.Group as={Col} controlId="promotionReason">
                  <Form.Label>Reason</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={promotionReason}
                    onChange={(e) => setPromotionReason(e.target.value)}
                  />
                </Form.Group>
              </Row>
              <Button variant="primary" type="submit">
                Submit Request
              </Button>
            </Form>
          )}
        </>
      )}
      {(currentUser?.userLevel === 'contributor' || currentUser?.userLevel === 'admin') && (
        <>
          <h1 className="create-title">Create a Post</h1>
          <div className="create">
            <form id="new_post" onSubmit={handleCreatePost}>
              <div className="content">
                <input
                  type="text"
                  id="name"
                  name="title"
                  placeholder="Title"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
                <div className="editor-container">
                  <ReactQuill className="editor" theme="snow" value={body} onChange={setBody} />
                </div>
              </div>
              <div className="menu">
                <div className="menu-item col-md-auto">
                  <Button as="input" type="submit" value="Publish" variant="primary" size="lg" />{' '}
                </div>
              </div>
            </form>
          </div>
        </>
      )}
    </>
  )}
</>
);
};

export default CreatePost;
