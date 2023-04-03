// src/BuildList.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BuildList = () => {
  const [builds, setBuilds] = useState([]);

//   useEffect(() => {
//     const fetchBuilds = async () => {
//       try {
//         const response = await axios.get('/api/builds');
//         setBuilds(response.data);
//       } catch (error) {
//         console.error('Error fetching build data:', error);
//       }
//     };

//     fetchBuilds();
//   }, []);

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
