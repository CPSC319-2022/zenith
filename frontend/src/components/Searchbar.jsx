import React, { useState } from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { InputGroup } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { postSliceActions } from '../redux/slices/postSlice';
import { useNavigate } from 'react-router-dom';

function Searchbar() {
    const [searchQuery, setSearchQuery] = useState('');
    const [sortBy, setSortBy] = useState('new');
    const dispatch = useDispatch();
    const navigate = useNavigate();

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
            navigate('/searchResults');
        }
    };

    return (
        <div className="searchForm">
            <Form onSubmit={handleSearchQuerySumbit}>
                <InputGroup >
                    <Form.Control onChange={handleSearchQuery} />
                    <Form.Group className="mb-0">
                        <Form.Select variant="outline-secondary" onChange={handleSortBy}>
                            <option value={'new'}>New</option>
                            <option value={'old'}>Old</option>
                            <option value={'top'}>Top</option>
                            <option value={'view'}>Views</option>
                        </Form.Select>
                    </Form.Group>
                    <Button variant="outline-secondary" id="button-addon2" type="submit">
                        Search
                    </Button>
                </InputGroup>
            </Form>
        </div>
    );
}
//<Form.Label column="sm" lg={5}>Sort By</Form.Label>

export default Searchbar;