import React, { useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import { AiFillLike, AiFillDislike, AiOutlineDelete, AiOutlineEdit } from 'react-icons/ai';
import ReactQuill from 'react-quill';

const emojis = ['😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇'];

const getRandomEmoji = () => {
  return emojis[Math.floor(Math.random() * emojis.length)];
};

const Comment = ({ comment, onUpvote, onDownvote, onEdit, onDelete }) => {
  const [emoji] = useState(getRandomEmoji());
  const [editing, setEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(comment.content);

  const handleEdit = () => {
    setEditedContent(comment.content);
    setEditing(true);
  };

  const handleSave = () => {
    // Call a function to save the edited comment content
    onEdit(editedContent.toString());
    setEditing(false);
  };

  const handleCancel = () => {
    setEditing(false);
  };

  const handleChange = (value) => {
    console.log(value);
    console.log("editedContent", editedContent);
    setEditedContent(value);
  };
  // delete this comment


  return (
      <Card className="mb-4">
        {/* card */}
        <Card.Body>
          <div className="d-flex justify-content-between">
            <div>
              <span className="emoji">{emoji}</span>
            </div>
            <Card.Subtitle className="text-muted">
              {new Date(comment.creationDate).toLocaleString().split(',')[0]}
            </Card.Subtitle>
          </div>
          {editing ? (
               <div className="editor-container">
               <ReactQuill className="editor" theme="snow" value={editedContent} onChange={handleChange} />
             </div>
          ) : (
              <Card.Text dangerouslySetInnerHTML={{ __html: comment.content }}></Card.Text>
          )}

          <div className="d-flex justify-content-between">
            <Button variant="outline-primary" onClick={onUpvote}>
              <AiFillLike /> {comment.upvotes}
            </Button>
            <Button variant="outline-danger" onClick={onDownvote}>
              <AiFillDislike /> {comment.downvotes}
            </Button>
            {editing ? (
                <>
                  <Button variant="success" onClick={handleSave}>
                    Save
                  </Button>
                  <Button variant="secondary" onClick={handleCancel}>
                    Cancel
                  </Button>
                </>
            ) : (
                <Button variant="info" onClick={handleEdit}>
                  <AiOutlineEdit />
                </Button>
            )}
            <Button variant="danger" onClick={onDelete}>
              <AiOutlineDelete />
            </Button>
          </div>
        </Card.Body>
      </Card>
  );
};

export default Comment;

