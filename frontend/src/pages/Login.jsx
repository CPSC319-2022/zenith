import React from 'react';
import { Card, Button, Container } from 'react-bootstrap';
import '../styles/Login.css';
import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';
import { useDispatch, useSelector } from 'react-redux';
import { setAuthenticated, setUser, setClientId, resetAuth } from '../redux/slices/auth';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  const handleSuccess = (credentialResponse) => {
    console.log('typeof credentialResponse:', typeof credentialResponse);
    console.log('Login Success', credentialResponse);
    localStorage.setItem('accessToken', credentialResponse.credential);
    localStorage.setItem('auth', JSON.stringify(credentialResponse));
    dispatch(setAuthenticated(true));
    dispatch(setUser(credentialResponse.profileObj));
    dispatch(setClientId(credentialResponse.clientId)); // update the clientId in the store
  };


  const handleFailure = (response) => {
    console.log('Logout Failed', response);
  };




  return (
    <Container className="d-flex justify-content-center align-items-center login-container">
      <Card className="login-card">
        <Card.Body>
          <Card.Title className="login-card-title">Welcome to Zenith Blog</Card.Title>
          {isAuthenticated ? (
            navigate('/')

          ) : (
            <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID} cookiePolicy={'single_host_origin'}>
              <GoogleLogin onSuccess={handleSuccess} onFailure={handleFailure} isSignedIn={true}>
                <Button variant="primary" className="rounded-pill login-button">
                  Sign In with Google
                </Button>
              </GoogleLogin>
            </GoogleOAuthProvider>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Login;