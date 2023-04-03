# Stage 0 - Build Frontend Assets
FROM node:19.6.0-alpine as build

ARG ENVIRONMENT=local
ENV ENV_FILE=.env.${ENVIRONMENT}

WORKDIR /app
COPY /package*.json ./
RUN npm install --legacy-peer-deps
COPY / .

# Copy the appropriate environment file
COPY ${ENV_FILE} .env

RUN npm run build

FROM build as test
ENV CI=true
# RUN ["npm", "run", "test", "--passWithNoTests"]   # TODO: UNCOMMENT THIS ONCE FRONT END TESTS ADDED

# Stage 1 - Serve Frontend Assets
FROM fholzer/nginx-brotli:v1.12.2
WORKDIR /etc/nginx

# Define build argument and set it as an environment variable
ARG ENVIRONMENT=local
ENV ENVIRONMENT=${ENVIRONMENT}

COPY nginx.local.conf /etc/nginx/
COPY nginx.cloud.conf /etc/nginx/

RUN if [ "$ENVIRONMENT" = "main" ] || [ "$ENVIRONMENT" = "qa" ] || [ "$ENVIRONMENT" = "prod" ]; then cp /etc/nginx/nginx.cloud.conf /etc/nginx/nginx.conf ; else cp /etc/nginx/nginx.local.conf /etc/nginx/nginx.conf ; fi
COPY --from=test /app/build /usr/share/nginx/html
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
