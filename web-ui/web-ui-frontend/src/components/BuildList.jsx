import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BuildList = () => {
  const [builds, setBuilds] = useState([]);

  useEffect(() => {
    const getTester = async () => {
      try {
        const response = await axios.get('https://web-ui-backend-t27edy3enq-uc.a.run.app/api/');
        console.log(response.data);
      } catch (error) {
        console.error('Error fetching tester data:', error);
      }
    };
    getTester();

    // WebSocket connection
    const ws = new WebSocket('wss://web-ui-backend-t27edy3enq-uc.a.run.app');
    ws.onopen = (event) => {
      console.log('WebSocket connection opened:', event);
    };
    ws.onmessage = (event) => {
      console.log('WebSocket message received:', event.data);
      const buildData = JSON.parse(event.data);
      setBuilds((prevBuilds) => [buildData, ...prevBuilds]);
    };

    // Clean up WebSocket connection when component is unmounted
    return () => {
      ws.close();
    };
  }, []);

  return (
    <ul>
      {builds.map((build) => (
        <li key={build.id}>
          <div>
            <strong>Build ID:</strong> {build.id}
          </div>
          <div>
            <strong>Status:</strong> {build.status}
          </div>
          <div>
            <strong>Start Time:</strong> {build.createTime}
          </div>
          <div>
            <strong>End Time:</strong> {build.finishTime}
          </div>
        </li>
      ))}
    </ul>
  );
};

export default BuildList;
