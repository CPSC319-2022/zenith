import React from 'react';
import {Button, Form} from "react-bootstrap";
import '../styles/Auth.css';
import {Link} from "react-router-dom";

const Register = () => {
    return (
        <Form className="auth">
            <h1> Zenith </h1>
            <h3> Register </h3>
            <hr/>
            <Form.Group className="mb-3">
                <Form.Control className="input" type="text" placeholder="username" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Control className="input" type="email" placeholder="email" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Control className="input" type="password" placeholder="password" />
                <Button className="auth-button" variant="primary" type="submit">
                    Register
                </Button>

                <span className="span">
                    Have an account?
                    <br/>
                    <Link to="/login"> Login </Link>
                </span>
            </Form.Group>
        </Form>
    );
};

export default Register;