# Base image
FROM node:19.6.0-alpine

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose the port your backend listens on
EXPOSE 9000

# Start the application
CMD ["npm", "start"]
