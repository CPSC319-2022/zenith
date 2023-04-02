import React from 'react';
import { Card, Button, Container } from 'react-bootstrap';
import '../styles/Login.css';
import { useDispatch, useSelector } from 'react-redux';
import { setAuthenticated, setUser, setClientId, resetAuth } from '../redux/slices/auth';
import { useNavigate } from 'react-router-dom';



const Logout = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);


    const handleFailure = (response) => {
        console.log('Logout Failed', response);
    };

    const handleSignOut = () => {
        window.localStorage.clear();
        dispatch(setAuthenticated(false));
        dispatch(setUser(null));
        dispatch(setClientId(null)); // reset the clientId in the store
        dispatch(resetAuth()); // reset the auth state in the store
        localStorage.removeItem('accessToken');
        localStorage.removeItem('auth');
    };


    return (
        <Container className="d-flex justify-content-center align-items-center login-container">
            <Card className="login-card">
                <Card.Body>
                    <Card.Title className="login-card-title">We hope that you are enjoying Zenith Blog</Card.Title>
                    {isAuthenticated ? (
                        <div>
                            <Card.Text className="login-card-text">
                                To Log Out Click the Red Button.
                            </Card.Text>
                            <Button variant="danger" onClick={handleSignOut} onFailure={handleFailure} className="login-button">
                                Logout
                            </Button>

                        </div>
                    ) : (

                        // window.location.href = '/'
                    navigate('/')


                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Logout;