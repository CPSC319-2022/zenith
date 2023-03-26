import React, { useState } from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { useDispatch } from 'react-redux';
import { postSliceActions } from '../redux/slices/postSlice';

function Searchbar() {
    const [searchQuery, setSearchQuery] = useState('');
    const [sortBy, setSortBy] = useState('New');
    const dispatch = useDispatch();

    const handleSearchQuery = (e) => {
        setSearchQuery(e.target.value.trimStart());
    };

    const handleSortBy = (e) => {
        setSortBy(e.target.value);
    };

    const handleSearchQuerySumbit = (e) => {
        e.preventDefault();
        if (searchQuery.trim().length !== 0) {
            console.log({searchQuery, sortBy});
            dispatch(postSliceActions.filterPosts({ searchQuery, sortBy }));
        }
    };

    return (
        <div>
            <Form onSubmit={handleSearchQuerySumbit}>
                <Row>
                    <Col xs={'auto'}>

                            <Form.Control onChange={handleSearchQuery} />

                    </Col>
                    <Col xs={'auto'}> 
                        <Form.Group className="mb-0">
                            <Form.Label column="sm" lg={5}>Sort By</Form.Label>
                            <Form.Select size="sm" onChange={handleSortBy}>
                            <option>new</option>
                            <option>old</option>
                            <option>top</option>
                            <option>view</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Button variant="primary" type="submit">
                            Search
                        </Button>
                    </Col>
                </Row>
            </Form>
        </div>
    );
}

export default Searchbar;