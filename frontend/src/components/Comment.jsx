import React from 'react';
import { Card, Button, Image, Row, Col } from 'react-bootstrap';
import { AiFillLike, AiFillDislike, AiOutlineEdit, AiOutlineDelete } from 'react-icons/ai';
import '../styles/Comment.css';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const Comment = ({
  comment,
  onUpvote,
  onDownvote,
  handleEdit,
  handleSave,
  handleCancel,
  onDelete,
  editing,
  editedContent,
  handleChange,
  userIsCommentAuthor,
  userIsAdmin,
}) => {
  return (
    
    <Card className="mb-4 comment-card">
      {console.log('comment', userIsCommentAuthor)}
      <Card.Body>
        <Row className="align-items-center">
          <Col xs={2} md={1} className="text-center">
            <Image src={comment.authorProfilePicture} roundedCircle className="comment-thumbnail" />
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
  