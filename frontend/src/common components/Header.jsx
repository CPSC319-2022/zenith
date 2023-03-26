import React from 'react';
import { Container, Nav, Navbar } from "react-bootstrap";
import blogImg from '../images/icon.png'
import '../styles/Header.css';
import { useSelector } from 'react-redux';
// import { fetchUser } from '../redux/slices/userSlice'; 

const Header = () => {
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    return (
        <Navbar className='nav-bar' bg="light" expand="lg">
            <Container>
                <Navbar.Brand href="/">
                    <img
                        src={blogImg}
                        width="130"
                        height="90"
                        className="d-inline-block align-top"
                        alt="zenith-blog-logo"
                    />
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ms-auto" placement="end">
                        <Nav.Link href="/">HOME</Nav.Link>
                        <Nav.Link href="/create">CREATE</Nav.Link>
                        {isAuthenticated && <Nav.Link href="/profile">PROFILE</Nav.Link>}     
                        {isAuthenticated && <Nav.Link href="/admin">ADMIN</Nav.Link>}                
                            <Nav.Link href="/wip">WIP</Nav.Link>
                        {
                            isAuthenticated ?
                            <Nav.Link href="/login">LOGOUT</Nav.Link> :
                            <Nav.Link href="/login">LOGIN</Nav.Link>
                        }
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Header;
