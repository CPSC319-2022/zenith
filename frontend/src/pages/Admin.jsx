import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchPromotionRequests, promoteUser, deleteRequest } from '../redux/slices/adminSlice';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const AdminPage = () => {
  const dispatch = useDispatch();
  const promotionRequests = useSelector((state) => state.admin.promotionRequests);
  const loading = useSelector((state) => state.admin.loading);
  const error = useSelector((state) => state.admin.error);

  useEffect(() => {
    // Fetch promotion requests here
    dispatch(fetchPromotionRequests({requestIDStart:0, count:100, reverse:false}));
  }, [dispatch]);

  const handleApprove = (row) => {
    // Promote user
    dispatch(promoteUser({ userID: row.userID, target: row.target }))
      .then(() => {
        // Refetch promotion requests after a successful approve request
        dispatch(fetchPromotionRequests({requestIDStart:0, count:100, reverse:false}));
      });
  };

  const handleDeny = (row) => {
    // Delete promotion request
    dispatch(deleteRequest(row.requestID))
      .then(() => {
        // Refetch promotion requests after a successful deny request
        dispatch(fetchPromotionRequests({requestIDStart:0, count:100, reverse:false}));
      });
  };

  return (
    <div className="mt-4">
      {loading && <div className="d-flex justify-content-center"><Spinner animation="border" variant="primary" /></div>}
      {error && <Alert variant="danger">{error}</Alert>}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Request ID</th>
            <th>User ID</th>
            <th>Username</th>
            <th>Target</th>
            <th>Request Time</th>
            <th>Reason</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {promotionRequests.map((row) => (
            <tr key={row.requestID}>
              <td>{row.requestID}</td>
              <td>{row.userID}</td>
              <td>
                <Link to={`/profile/${row.userID}`}>
                  {row.username}
                </Link>
              </td>
              <td>{row.target}</td>
              <td>{new Date(row.requestTime).toLocaleString()}</td>
              <td>{row.reason}</td>
              <td>
                <Button
                  variant="success"
                  size="sm"
                  onClick={() => handleApprove(row)}
                >
                  Approve
                </Button>{' '}
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDeny(row)}
                >
                  Deny
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default AdminPage;
