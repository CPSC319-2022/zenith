import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useSelector,useDispatch } from 'react-redux';
import { selectIsAuthenticated } from '../redux/slices/auth';
import { userSliceActions, fetchCurrentUserByToken } from '../redux/slices/userSlice';
import AdminPage from './Admin';


const AdminWrapper = () => {
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

  return (
    <div>
        {user && user.userLevel === 'ADMIN' ? (<AdminPage />) : (<div>Not Authorized</div>)}
    </div>
  );
};

export default AdminWrapper;
