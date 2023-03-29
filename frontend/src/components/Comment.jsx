import React, {useState} from 'react';
import { Card, Button, Image, Row, Col } from 'react-bootstrap';
import { AiFillLike, AiFillDislike, AiOutlineEdit, AiOutlineDelete } from 'react-icons/ai';
import '../styles/Comment.css';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { Link } from 'react-router-dom';

const Comment = ({
  comment,
  onUpvote,
  userIsLoggedIn,
  onDownvote,
  
  // handleCancel,
  onDelete,
  onEdit,
  userIsCommentAuthor,
  userIsAdmin,
 
}) => {
  
  const [editing, setEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(comment.content);
  
  const handleEdit = () => {
    setEditedContent(comment.content);
    setEditing(true);
  };
  const handleChange = (value) => {
    console.log(value);
    console.log("editedContent", editedContent);
    setEditedContent(value);
  };
  const handleSave = () => {
    // Call a function to save the edited comment content
    onEdit(editedContent.toString());
    setEditing(false);
  };

  const handleCancel = () => {
    setEditing(false);
  };
  return (
    

    <Card className="mb-4 comment-card">
      {console.log('comment', userIsCommentAuthor)}
      <Card.Body>
        <Row className="align-items-center">
          <Col xs={2} md={1} className="text-center">
            <Link to={`/profile/${comment.authorID}`}>
              <img className="comment-thumbnail" src={comment.authorProfilePicture} alt={comment.authorProfilePicture} referrerpolicy="no-referrer"/>
            </Link>
          </Col>
          <Col>
            <Card.Title className="comment-author">{comment.authorUsername}</Card.Title>
            <Card.Subtitle className="mb-2 text-muted">
              {new Date(comment.creationDate).toLocaleString().split(',')[0]}
            </Card.Subtitle>
          </Col>
        </Row>
        {editing ? (
          <div className="editor-container">
            <ReactQuill className="editor" theme="snow" value={editedContent} onChange={handleChange} />
          </div>
        ) : (
          <Card.Text className="comment-text" dangerouslySetInnerHTML={{ __html: comment.content }}></Card.Text>
        )}
  
        <div className="d-flex justify-content-between">
          {userIsLoggedIn && (
          <Button variant="outline-primary" onClick={onUpvote}>
            <AiFillLike /> {comment.upvotes}
          </Button>
          )}
          {userIsLoggedIn && (
          <Button variant="outline-danger" onClick={onDownvote}>
            <AiFillDislike /> {comment.downvotes}
          </Button>
          )}
          {!userIsLoggedIn && (
          <Button variant="outline-primary" disabled>
            <AiFillLike /> {comment.upvotes}
          </Button>
          )}
          {!userIsLoggedIn && (
          <Button variant="outline-danger" disabled>
            <AiFillDislike /> {comment.downvotes}
          </Button>
          )}
             
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
            <>
              {userIsCommentAuthor && (
                <Button variant="info" onClick={handleEdit}>
                  <AiOutlineEdit />
                </Button>
              )}
              {(userIsCommentAuthor || userIsAdmin) && (
                <Button variant="danger" onClick={onDelete}>
                  <AiOutlineDelete />
                </Button>
              )}
            </>
          )}
        </div>
      </Card.Body>
    </Card>
  );
              }
  export default Comment;