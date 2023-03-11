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

    function dispatchInput(e) {
        e.preventDefault();
        
        const body = new FormData(e.target);

        dispatch(postSliceActions.createPost({ body }));

        setTitle('');
    }

//const Create = () => {
//    const [value, setValue] = useState('');

    return (
        <div className="create">
            <form
                id="new_post"
                onSubmit={dispatchInput}
            >
                <div className="content">
                    <input type="text" id="name" name="title" placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)}/>
                    <div className="editor-container">
                        <ReactQuill className="editor" theme="snow" value={value} onChange={setValue} />
                    </div>
                </div>
                <div className="menu">
                    <div className="menu-item col-md-auto">
                    
                        <Button as="input" type="submit" value="Submit" variant="primary" size="lg">Publish</Button>{' '}
                    
                        <Form.Group controlId="formFile" >
                            
                            <Form.Control type="file" />
                        </Form.Group>
                    
                        <Button variant="outline-warning" >Save Draft</Button>{' '}
                        <Button variant="outline-success">Update</Button>{' '}
                        
                    </div>
                </div>
            </form>
        </div>
    );
};

//export default Create;