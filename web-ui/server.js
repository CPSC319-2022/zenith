const express = require('express');
const { Server } = require('ws');

const app = express();
const PORT = process.env.PORT || 9000;

app.post('/api/post-status', (req, res) => {
  console.log(req.body);
  res.send('request received:' + req.body);
});

const server = app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`);
});

// WebSocket server
const wss = new Server({ server });

wss.on('connection', (ws) => {
  console.log('Client connected');

  ws.on('close', () => {
    console.log('Client disconnected');
  });

  // Broadcast incoming notifications to all connected clients
  app.post('/api/post-status', (req, res) => {
    console.log(req.body);

    wss.clients.forEach((client) => {
      client.send(JSON.stringify(req.body));
    });

    res.send('request received:' + req.body);
  });
});
