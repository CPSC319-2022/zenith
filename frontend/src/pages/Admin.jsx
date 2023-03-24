import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchPromotionRequests, promoteUser, deleteRequest } from '../redux/slices/adminSlice';
import BootstrapTable from 'react-bootstrap-table-next';
import cellEditFactory from 'react-bootstrap-table2-editor';
import { Button } from 'react-bootstrap';

const AdminPage = () => {
  const dispatch = useDispatch();
  const promotionRequests = useSelector((state) => state.admin.promotionRequests);
  const loading = useSelector((state) => state.admin.loading);
  const error = useSelector((state) => state.admin.error);

  useEffect(() => {
    // Fetch promotion requests here
    dispatch(fetchPromotionRequests({requestIDStart:0, count:10, reverse:false}));
  }, [dispatch]);

  const columns = [
    {
      dataField: 'requestID',
      text: 'Request ID',
    },
    {
      dataField: 'userID',
      text: 'User ID',
    },
    {
      dataField: 'target',
      text: 'Target',
    },
    {
      dataField: 'requestTime',
      text: 'Request Time',
    },
    {
      dataField: 'reason',
      text: 'Reason',
    },
    {
      dataField: 'actions',
      text: 'Actions',
      isDummyField: true,
      editable: false,
      formatter: (cell, row) => (
        <>
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
        </>
      ),
    },
  ];

  return (
    <div>
      {loading && <div>Loading...</div>}
      {error && <div>{error}</div>}
      <BootstrapTable
        keyField="requestID"
        data={promotionRequests}
        columns={columns}
        cellEdit={cellEditFactory({ mode: 'click', blurToSave: true })}
        striped
        hover
        condensed
      />
    </div>
  );
};

export default AdminPage;
