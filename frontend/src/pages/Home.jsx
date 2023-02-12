import React from 'react';
import {fakePosts} from '../data/fakePosts';
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import "../styles/Home.css"

const Home = () => {
    return (
        <div className="home">
            <div className="posts">
                {fakePosts.map(post => (
                    <div className="post" key={post.id}>
                        <div className="img">
                            <img src={post.img} alt=""/>
                        </div>

                        <div className="content">
                            <Link className="link" to={`/single-post/${post.id}`}>
                                <h1 className="post-title">{post.title}</h1>
                            </Link>
                            <p>{post.content}</p>
                            <Button className="read-button">Read More</Button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Home;