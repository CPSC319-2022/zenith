import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchPromotionRequests, promoteUser, deleteRequest } from '../redux/slices/adminSlice';
import { Table, Button } from 'react-bootstrap';

const AdminPage = () => {
  const dispatch = useDispatch();
  const promotionRequests = useSelector((state) => state.admin.promotionRequests);
  const loading = useSelector((state) => state.admin.loading);
  const error = useSelector((state) => state.admin.error);

  useEffect(() => {
    // Fetch promotion requests here
    dispatch(fetchPromotionRequests({requestIDStart:0, count:10, reverse:false}));
  }, [dispatch]);

  return (
    <div>
      {loading && <div>Loading...</div>}
      {error && <div>{error}</div>}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Request ID</th>
            <th>User ID</th>
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
              <td>{row.target}</td>
              <td>{row.requestTime}</td>
              <td>{row.reason}</td>
              <td>
                <Button
                  variant="success"
                  size="sm"
                  onClick={() => {
                    // Promote user
                    dispatch(promoteUser({ userID: row.userID, target: row.target }));
                  }}
                >
                  Approve
                </Button>{' '}
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => {
                    // Delete promotion request
                    dispatch(deleteRequest(row.requestID));
                  }}
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
