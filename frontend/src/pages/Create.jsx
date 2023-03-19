import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { postSliceActions } from '../redux/slices/postSlice';
import { setClientId } from '../redux/slices/auth';

import { Link } from 'react-router-dom';

const CreatePost = () => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  console.log('isAuthenticated:', isAuthenticated);
  const clientID = useSelector((state) => state.auth.user?.clientId);
  console.log('clientIDCreate:', clientID);
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');

  const handleCreatePost = (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      return alert('Please login to create a post.');
    }
   

    // Replace authorID with the clientID of the currently logged-in user
    const allowComments = true; // You can change this according to your requirements

    dispatch(
      postSliceActions.createPost({
        authorID: clientID,
        title,
        content: body,
        allowComments,
      })
    );

    setTitle('');
    setBody('');
  };

  return (
    <>
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
              <Form.Group controlId="formFile">
                <Form.Control type="file" />
              </Form.Group>
              <Button variant="outline-success">Update</Button>{''}
            </div>
          </div>
        </form>
      </div>
    </>
  );
};

export default CreatePost;
