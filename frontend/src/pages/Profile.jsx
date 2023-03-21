import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { userSliceActions } from '../redux/slices/userSlice';
import "../styles/Profile.css";
import avatar from "../images/avatar.jpg";

const Profile = () => {
  const { id } = useParams();
  const dispatch = useDispatch();
  const [user, setUser] = useState(null);
  const userData = useSelector((state) => state.user?.user);

  useEffect(() => {
    dispatch(userSliceActions.fetchUser(id)).then((response) => {
      if (response.meta.requestStatus === 'fulfilled') {
        setUser(response.payload);
      }
    });
  }, [id, dispatch]);

  const creationDate = user?.creationDate ? new Date(user.creationDate).toLocaleDateString() : '';
  const lastLogin = user?.lastLogin ? new Date(user.lastLogin).toLocaleDateString() : '';

  if (!user) {
    return <div>Loading...</div>;
  }
  

  return (
    <div className="profile">
      <img className="avatar"
        src={user.profilePicture || avatar}
        width= "190"
        height="190"
        alt="React Bootstrap logo"
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
