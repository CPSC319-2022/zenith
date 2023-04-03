const express = require('express');
const cors = require('cors');


const app = express();
app.use(cors());
const port = process.env.PORT || 9000;

// Root endpoint
app.get('/api/', (req, res) => {
  res.send('Hello, world!');
});
app.post('/postStatus', (req, res) => {

});

// Start the server
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
