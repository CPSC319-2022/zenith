import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import "../styles/Profile.css";
import avatar from "../images/avatar.jpg";

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

  
  // ...
  

  const creationDate = user?.creationDate ? new Date(user.creationDate).toLocaleDateString() : '';
  const lastLogin = user?.lastLogin ? new Date(user.lastLogin).toLocaleDateString() : '';

  if (!user) {
    return <div>Please Log In to view Profile...</div>;
  }

  return (
    <div className="profile">
      <img className="avatar"
        src={user.profilePicture || avatar}
        width= "190"
        height="190"
        alt="React Bootstrap logo"
        referrerPolicy="no-referrer"
      />
      <br/>
      <h3 className="name">{user.username}</h3>
      <br/>
      <h5>{user.userLevel}</h5>
      <br/>
      <p>Account creation date: {creationDate}</p>
      <p>Last login: {lastLogin}</p>
      {/* ... other elements */}
    </div>
  );
};

export default Profile;
