import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import '../styles/Create.css';
import Button from 'react-bootstrap/Button';
import { postSliceActions } from '../redux/slices/postSlice';
import { Link, useParams } from 'react-router-dom';

const CreatePost = () => {
   const { id } = useParams();
  const dispatch = useDispatch();
  const [user, setUser] = useState(null);
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');

  console.log('id:',id);

  useEffect(() => {
    if (id) {
      dispatch(userSliceActions.fetchUser(id)).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          setUser(response.payload);
        }
      });
    } else if (isAuthenticated) {
      dispatch(fetchCurrentUserByToken()).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          setUser(response.payload);
        }
      });
    }
  }, [id, dispatch, isAuthenticated]);

    if (!isAuthenticated) {
      return (
        <div className="login-button">
          <Link to="/login">
            <Button variant="primary">Login to Create a Post</Button>
          </Link>
        </div>
      );
    }
  
    if (user&&user.userLevel === 'READER') {
      console.log('this:',user.userLevel);
        return (
          <div>
            <h1>Not Authorized</h1>
            <p> You need to be a Contributor or an Admin to create a post. See Profile Page for applying to become one!</p>
          </div>
        )
      }

  const handleCreatePost = (e) => {
    e.preventDefault();
    if (!isAuthenticated) {
      return alert('Please login to create a post.');
    }
   
   

    // Replace authorID with the clientID of the currently logged-in user
    const allowComments = true; // You can change this according to your requirements

    dispatch(
      postSliceActions.createPost({
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
        {user && (user.userLevel === 'ADMIN' || user.userLevel === 'CONTRIBUTOR') ? ( 
          <>
            <h1 className="create-title">Create a Post</h1>
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
                    {/* <Form.Group controlId="formFile">
                      <Form.Control type="file" />
                    </Form.Group> */}
                    {/* <Button variant="outline-success">Update</Button>{''} */}
                  </div>
                </div>
              </form>
            </div>
          </>
        ) : (<div>Not Authorized</div>)}
      </>
    );
    
};

export default CreatePost;