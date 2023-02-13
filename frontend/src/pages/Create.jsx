import React, {useState} from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';


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
                <div className="menu-item">
                    {/* <h1 className="publish">Publish</h1> */}
                    <Button variant="primary">Publish</Button>{' '}
                    <div className="visibility">
                        <b> Visibility:</b> Public
                    </div>
                    <br/>
                    <input style={{display: 'none'}} type="file" id="file" />
                    <label className="img-upload" htmlFor="file">Upload Image</label>
                    <div className="buttons">
                        <button className="btn1">Save Draft</button>
                        <button className="btn2">Update</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Create;