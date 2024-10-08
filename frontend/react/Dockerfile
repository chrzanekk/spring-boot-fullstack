#FROM FULLSTACK COURSE

#FROM node:22-alpine
#ARG api_base_url
#WORKDIR /app
#COPY package*.json .
#RUN npm i --silent
#COPY . .
#RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
#EXPOSE 5173
#CMD ["npm", "run", "dev"]

# Step 1: Build the React app
FROM node:22-alpine AS build
ARG api_base_url
WORKDIR /app
COPY package*.json ./
RUN npm install --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
RUN npm run build

# Step 2: Serve the build with Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]