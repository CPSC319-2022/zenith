import React from 'react';
import { Card, Button } from 'react-bootstrap';
import '../styles/Auth.css';
import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';
import { useDispatch, useSelector } from 'react-redux';
import { setAuthenticated, setUser } from '../redux/slices/auth';

const Login = () => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  const handleSuccess = (credentialResponse) => {
    console.log('Login Success', credentialResponse);
    dispatch(setAuthenticated(true));
    dispatch(setUser(credentialResponse.profileObj));
  };

  const handleFailure = (response) => {
    console.log('Login Failed', response);
  };

  const handleSignOut = () => {
    dispatch(setAuthenticated(false));
    dispatch(setUser(null));
  };

  return (
    <div className="d-flex justify-content-center align-items-center h-100">
      <Card style={{ width: '25rem' }}>
        <Card.Body>
          <Card.Title>Welcome to Zenith Blog</Card.Title>
          {isAuthenticated ? (
            <div>
              <p>You are already signed in.</p>
              <Button variant="danger" onClick={handleSignOut}>
                Logout
              </Button>
            </div>
          ) : (
            <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID} cookiePolicy={'single_host_origin'}>
              <GoogleLogin onSuccess={handleSuccess} onFailure={handleFailure}>
                <Button variant="primary" className="rounded-pill">
                  Sign In with Google
                </Button>
              </GoogleLogin>
            </GoogleOAuthProvider>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};

export default Login;
