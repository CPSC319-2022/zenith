import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import "../styles/Profile.css";
import avatar from "../images/avatar.jpg";
import UpgradeRequestForm from '../components/UpgradeRequestForm';
import { Button } from 'react-bootstrap'; 

const Profile = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const [user, setUser] = useState(null);
  const isAuthenticated = useSelector(selectIsAuthenticated);

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
        {(!id && (user.userLevel === 'READER' || user.userLevel === 'CONTRIBUTOR')) && (
          showForm ? (
            <UpgradeRequestForm user={user} onClose={handleCloseForm} />
          ) : (
            <Button variant="primary" onClick={() => setShowForm(true)}>Request Level Upgrade</Button>
          )
        )}
      </div>
    </div>
  );
};

export default Profile;
