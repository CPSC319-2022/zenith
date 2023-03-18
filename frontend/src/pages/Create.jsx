import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { postSliceActions } from '../redux/slices/postSlice';

export default function CreatePost() {
  const dispatch = useDispatch();

  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');

  function dispatchInput(e) {
    e.preventDefault();

    // Replace this line with the authorID of the currently logged-in user
    const authorID = 1;
    const allowComments = true; // You can change this according to your requirements

    dispatch(
      postSliceActions.createPost({
        authorID,
        title,
        content: body,
        allowComments,
      })
    );

    setTitle('');
    setBody('');
  }

  return (
    <div className="create">
      <form id="new_post" onSubmit={dispatchInput}>
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
            <ReactQuill
              className="editor"
              theme="snow"
              value={body}
              onChange={setBody}
            />
          </div>
        </div>
        <div className="menu">
          <div className="menu-item col-md-auto">
            <Button
              as="input"
              type="submit"
              value="Publish"
              variant="primary"
              size="lg"
            />{' '}
            <Form.Group controlId="formFile">
              <Form.Control type="file" />
            </Form.Group>
            <Button variant="outline-success">Update</Button>{' '}
          </div>
        </div>
      </form>
    </div>
  );
}
