import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import {fakePosts} from '../data/fakePosts';
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import "../styles/Home.css"
import Pagination from "./Pagination";

const Home = () => {
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage, setPostsPerPage] = useState(4);

    const lastPostIndex = currentPage * postsPerPage;
    const firstPostIndex = lastPostIndex - postsPerPage;
    const currentPosts = fakePosts.slice(firstPostIndex, lastPostIndex);

    const navigate = useNavigate();
    function handleClick(id) {
        navigate(`/single-post/${id}`);
    }

    return (
        <div className="home">
            <div className="posts">
                {currentPosts.map(post => (
                    <div className="post" key={post.id}>
                        <div className="img">
                            <img src={post.img} alt=""/>
                        </div>

                        <div className="content">
                            <Link className="link" to={`/single-post/${post.id}`}>
                                <h1 className="post-title">{post.title}</h1>
                            </Link>
                            <p className="post-content">{post.content}</p>
                            <Button className="read-button"
                                    onClick={() => handleClick(`${post.id}`)}>Read More</Button>
                        </div>
                    </div>
                ))}
            </div>

            <Pagination
                totalPosts={fakePosts.length}
                postsPerPage={postsPerPage}
                setCurrentPage={setCurrentPage}
                currentPage={currentPage}
            />
        </div>
    );
};

export default Home;