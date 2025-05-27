# back/api-gateway/Dockerfile
FROM node:20
WORKDIR /app
COPY api-gateway/package*.json ./
RUN npm install
COPY api-gateway/ ./
CMD ["npm","run","start:dev"]
