import React, { useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';


const Create = () => {
    const [value, setValue] = useState('');

    return (
        <div className="create">

            <div className="content">
                <input type="text" name="title" placeholder="Title" />
                <div className="editor-container">
                    <ReactQuill className="editor" theme="snow" value={value} onChange={setValue} />
                </div>
            </div>
            <div className="menu">
                <div className="menu-item col-md-auto">
                  
                    <Button variant="primary" size="lg">Publish</Button>{' '}
                  
                    <Form.Group controlId="formFile" >
                        
                        <Form.Control type="file" />
                    </Form.Group>
                   
                    <Button variant="outline-warning" >Save Draft</Button>{' '}
                    <Button variant="outline-success">Update</Button>{' '}
                    
                </div>
            </div>

        </div>
    );
};

export default Create;