import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import { fetchPostsByUser } from '../redux/slices/postSlice';
import "../styles/Profile.css";
import avatar from "../images/avatar.jpg";
import UpgradeRequestForm from '../components/UpgradeRequestForm';
import { Link } from 'react-router-dom';
import { Button, Card, Col, Row, Table } from 'react-bootstrap'; 

const Profile = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.userPosts);
  const [user, setUser] = useState(null);
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const [isUser, setIsUser] = useState(true);

  useEffect(() => {
    dispatch(fetchCurrentUserByToken())
      .then((res) => {
        if (id) {
          if (res.payload.userID != id){
            console.log("yes");
            setIsUser(false);
          }
        }
      });
    if (id) {
      dispatch(userSliceActions.fetchUser(id)).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          dispatch(fetchPostsByUser({ userID: response.payload.userID, count: 5 }))
          setUser(response.payload);
        }
      });
    } else if (isAuthenticated) {
      dispatch(fetchCurrentUserByToken()).then((response) => {
        if (response.meta.requestStatus === 'fulfilled') {
          dispatch(fetchPostsByUser({ userID: response.payload.userID, count: 5 }))
          setUser(response.payload);
        }
      });
    }
  }, [id, dispatch, isAuthenticated]);

  const [showForm, setShowForm] = useState(false);

  const handleCloseForm = () => {
    setShowForm(false);
  };

  const creationDate = user?.creationDate ? new Date(user.creationDate).toLocaleDateString() : '';
  //const lastLogin = user?.lastLogin ? new Date(user.lastLogin).toLocaleDateString() : '';

  if (!user) {
    return <div>Please Log In to view Profile...</div>;
  }

  return (
    <>
      <div className="profile">
        <div className="profile-container">
          <img className="avatar"
            src={user.profilePicture || avatar}
            alt="User avatar"
            referrerPolicy="no-referrer"
          />
          <h3 className="name">{user.username}</h3>
          <h5>{user.userLevel}</h5>
          <p>Account creation date: {creationDate}</p>
          {/*<p>Last login: {lastLogin}</p>*/}
          {(isUser && (user.userLevel === 'READER' || user.userLevel === 'CONTRIBUTOR')) && (
            showForm ? (
              <UpgradeRequestForm user={user} onClose={handleCloseForm} />
            ) : (
              <Button variant="primary" onClick={() => setShowForm(true)}>Request Level Upgrade</Button>
            )
          )}
        </div>
      </div>
      <div className="posts">
      <div className="posts-container">
        <h3 className="posts-header">Most Recent Posts</h3>
          {posts.length == 0 && 
            <div className="no-posts">
               <h5>This user hasn't made any posts yet</h5>
            </div>
          }
            {posts.map((post) => (
                  <Card className="col-md-4 col-sm-6 mb-4 post">
                    <Card.Header>{new Date(post.creationDate).toLocaleDateString()}</Card.Header>
                    <Row className="post-row">
                      <Col className="col-md-3 thumbnail-col">
                      <Link className="post-thumbnail" to={`/single-post/${post.postID}`}>
                      <Card.Img variant="top" src={post.thumbnailURL} />
                    </Link>
                      </Col>
                    <Col className="body-col">
                    <Card.Body className="post-body">
                      <Card.Title className="post-title">{post.title}</Card.Title>
                      <Card.Text className="post-content">
                        <div dangerouslySetInnerHTML={{ __html: post.content}}></div>
                      </Card.Text>
                      
                      <Button variant="primary">Read More</Button>
                      </Card.Body>
                    </Col>
                    </Row>
                  </Card>
            ))}
      </div>
      </div>
    </>
  );
};

export default Profile;
