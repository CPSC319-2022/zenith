import React from 'react';
import {Form} from "react-bootstrap";
import '../styles/Auth.css';
// import {Link} from "react-router-dom";
import {GoogleLogin, GoogleOAuthProvider} from "@react-oauth/google";


const Login = () => {
    return (
        <Form className="auth">
            <h1> Zenith </h1>
            <hr/>
            <GoogleOAuthProvider
                clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                cookiePolicy={'single_host_origin'}
            >
                <GoogleLogin
                    onSuccess={(credentialResponse) => {
                        console.log('*******************', credentialResponse);
                    }}
                    onError={() => {
                        console.log('Login Failed');
                    }}
                />
            </GoogleOAuthProvider>
        </Form>
    );
};

export default Login;