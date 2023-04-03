# Build React frontend
FROM node:14 AS frontend-builder
WORKDIR /app
COPY web-ui-frontend/package*.json ./
RUN npm ci
COPY web-ui-frontend/ ./
RUN npm run build

# Set up Node.js backend
FROM node:14 AS backend
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY server.js ./

# Set up Nginx
FROM nginx:1.21
COPY --from=frontend-builder /app/build /usr/share/nginx/html
COPY --from=backend /app /app
COPY nginx.template /etc/nginx/nginx.template
EXPOSE 80
CMD ["nginx", "-g", "daemon off; envsubst '\$PORT' < /etc/nginx/nginx.template > /etc/nginx/nginx.conf;"]
