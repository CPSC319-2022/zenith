const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');



const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
const port = process.env.PORT || 9000;



// Root endpoint
app.get('/api/', (req, res) => {
  res.send('Hello, world!');
});
app.post('/api/post-status', (req, res) => {
console.log(req.body);
res.send('request received:' + req.body);
});

// Start the server
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
