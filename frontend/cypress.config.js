const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    baseUrl: 'http://localhost:3000', // Replace with your frontend server address and port
    specPattern: 'cypress/integration/**/*.spec.js', // Specify the pattern for test files
  },
});
