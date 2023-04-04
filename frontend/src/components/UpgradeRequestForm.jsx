import React, { useState } from 'react';
import { Form, Row, Col, Button, FormLabel } from 'react-bootstrap';
import { promoteUser } from '../api';
import '../styles/UpgradeRequestForm.css';


const UpgradeRequestForm = ({ user, onClose }) => {
  const [promotionLevel, setPromotionLevel] = useState('Contributor');
  const [promotionReason, setPromotionReason] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [renderForm, setRenderForm] = useState(true);

  const resetMessages = () => {
    setErrorMessage('');
    setSuccessMessage('');
  };

  const handlePromotionRequest = async (e) => {
    e.preventDefault();
    try {
      await promoteUser({
        target: (user.userLevel === 'CONTRIBUTOR' ? 'Admin' : promotionLevel),
        reason: promotionReason,
      });
      setRenderForm(false);
      setSuccessMessage('Promotion request sent successfully.');
    } catch (error) {
      console.error('Error requesting promotion:', error);
      setErrorMessage('Error requesting promotion. Please try again later.');
    }
  };

  return (
    <>
    {renderForm &&
      <Form onSubmit={handlePromotionRequest} className="upgrade-request-form">
        <Row className="mb-3">
          <Form.Group as={Col} controlId="promotionLevel">
            <Form.Label>Promotion Level</Form.Label>
            {user.userLevel === 'READER' &&
              <Form.Select
              value={promotionLevel}
              onChange={(e) => setPromotionLevel(e.target.value)}
              >
                <option>Contributor</option>
                <option>Admin</option>  
              </Form.Select>
            }
            {user.userLevel === 'CONTRIBUTOR' &&
              <FormLabel>Admin</FormLabel>
            }

          </Form.Group>
        </Row>
        <Row className="mb-3">
          <Form.Group as={Col} controlId="promotionReason">
            <Form.Label>Reason</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={promotionReason}
              onChange={(e) => setPromotionReason(e.target.value)}
            />
          </Form.Group>
        </Row>
        <Button variant="primary" type="submit">
          Submit/Update Request
        </Button>
      </Form>
    }
      {errorMessage && (
        <div className="alert alert-danger">
          {errorMessage}
          <Button
            className="ml-2"
            variant="outline-danger"
            onClick={() => resetMessages()}
          >
            Retry
          </Button>
        </div>
      )}
      {successMessage && (
        <div className="alert alert-success">
          {successMessage}
          <Button
            className="ml-2"
            variant="outline-success"
            onClick={onClose}
          >
            Back to Profile
          </Button>
        </div>
      )}
    </>
  );
};

export default UpgradeRequestForm;
