import React, { useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const EditCommentForm = ({ postID, commentID, content, onSubmit }) => {
    const [editedContent, setEditedContent] = useState(content);

    const handleChange = (value) => {
        setEditedContent(value);
    };

    const handleSubmit = () => {
        onSubmit(editedContent);
    };

    return (
        <div>
            <ReactQuill value={editedContent} onChange={handleChange} />
            <button onClick={handleSubmit}>Save Changes</button>
        </div>
    );
};

const handleEditComment = async (postID, commentID, content) => {
    console.log('whats going to action', postID, commentID, content);

    const onSubmit = (editedContent) => {
        console.log('whats going to action', editedContent);
        dispatch(editComment({ postID, commentID, editedContent }));
    };

    return <EditCommentForm postID={postID} commentID={commentID} content={content} onSubmit={onSubmit} />;
};
