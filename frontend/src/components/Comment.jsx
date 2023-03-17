import React, { useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import { AiFillLike, AiFillDislike } from 'react-icons/ai';

const emojis = ['😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇'];

const getRandomEmoji = () => {
  return emojis[Math.floor(Math.random() * emojis.length)];
};

const Comment = ({ comment }) => {
  const [emoji] = useState(getRandomEmoji());

  return (
    <Card className="mb-4">
      <Card.Body>
        <div className="d-flex justify-content-between">
          <div>
            <span className="emoji">{emoji}</span>
            {/* <Card.Title className="d-inline ml-2">{comment.authorID}</Card.Title> */}
          </div>
          <Card.Subtitle className="text-muted">
            {new Date(comment.creationDate).toLocaleString()}
          </Card.Subtitle>
        </div>
        <Card.Text dangerouslySetInnerHTML={{ __html: comment.content }}></Card.Text>
        <div className="d-flex justify-content-between">
          <Button variant="outline-primary">
            <AiFillLike /> {comment.upvotes}
          </Button>
          <Button variant="outline-danger">
            <AiFillDislike /> {comment.downvotes}
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};

export default Comment;