import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link, useParams, useNavigate } from 'react-router-dom';
import Container from 'react-bootstrap/Container';

const CreatePost = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [image, setImage] = useState(null);

  useEffect(() => {
    if (id) {
      dispatch(userSliceActions.fetchUser(id)).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          setUser(response.payload);
        }
      });
    } else if (isAuthenticated) {
      dispatch(fetchCurrentUserByToken()).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          setUser(response.payload);
        }
      });
    }
  }, [id, dispatch, isAuthenticated]);

  const handleCreatePost = (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      return alert("Please login to create a post.");
    }
  
    const allowComments = true; // You can change this according to your requirements
  
    dispatch(
      postSliceActions.createPost({
        postData: {
          title,
          content: body,
          allowComments,
        },
        file: image, // Pass the image state
      })
    ).then((response) => {
      if (response.meta.requestStatus === 'fulfilled') {

        setTitle("");
        setBody("");
        window.location.href = `/`;
        // navigate(`/`);
      }});
  
 
  };
  


  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
  };

  return (
    <Container className="create-post-container">
    {user && (user.userLevel === 'ADMIN' || user.userLevel === 'CONTRIBUTOR') ? (
      <>
        <h1 className="create-post-title">Create a Post</h1>
        <Form onSubmit={handleCreatePost}>
          <Form.Group controlId="postTitle">
            <Form.Control
              type="text"
              name="title"
              placeholder="Title - Can not be changed later"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </Form.Group>
          <div className="editor-container">
            <ReactQuill className="editor" theme="snow" value={body} onChange={setBody} />
          </div>
          <Form.Group controlId="formFile">
            <Form.Label>Upload Image - (Optional) Max file size 1MB </Form.Label>
            <Form.Control type="file" onChange={handleImageChange} />
          </Form.Group>
          <Button type="submit" variant="primary" size="lg" className="submit-button">
            Publish
          </Button>
        </Form>
      </>
    ) :  (
      <>
        {!user ? (
          <div className="not-authorized-container">
            <p>You need to be logged in and be a creator to create a post</p>
            <Link to="/login">
              <Button variant="primary">Login</Button>
            </Link>
          </div>
        ) : user.userLevel === 'READER' ? (
          <div className="not-authorized-container">
            <p>You need to be a contributor to create a post</p>
            <Link to="/profile">
              <Button variant="primary">Apply to be a contributor</Button>
            </Link>
          </div>
        ) : null}
      </>
    )}
  </Container>
);
};

export default CreatePost;

