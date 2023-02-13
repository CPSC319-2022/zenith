import React from 'react';
import {Container, Nav, Navbar} from "react-bootstrap";
import blogImg from '../images/icon.png'
import '../styles/Header.css';

const Header = () => {
    return (
        <Navbar className="navbar">
            <Container className="header-container">
                <Navbar.Brand className="blog-img" href="/">
                    <img
                        src={blogImg}
                        width="130"
                        height="90"
                        className="d-inline-block align-top"
                        alt="React Bootstrap logo"
                    />
                </Navbar.Brand>
                <Nav className="ml-auto" placement="end">
                    <Nav.Link href="/">HOME</Nav.Link>
                    <Nav.Link href="/create">CREATE</Nav.Link>
                    <Nav.Link href="/profile">PROFILE</Nav.Link>
                    <Nav.Link href="/login">LOGOUT</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
    );
};

export default Header;