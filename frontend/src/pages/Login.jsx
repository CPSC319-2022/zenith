import React from 'react';
import {Button, Form} from "react-bootstrap";
import '../styles/Auth.css';
import {Link} from "react-router-dom";

const Login = () => {
    return (
        <Form className="auth">
            <h1> Zenith </h1>
            <hr/>
            <Form.Group className="mb-3">
                <Form.Control className="input" type="text" placeholder="username" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Control className="input" type="password" placeholder="password" />
                <Button className="auth-button" variant="primary" type="submit" formaction="/">
                    Login
                </Button>

                <span className="span">
                    Don't have an account?
                    <Link to="/register"> Register </Link>
                </span>
            </Form.Group>
        </Form>
    );
};

export default Login;