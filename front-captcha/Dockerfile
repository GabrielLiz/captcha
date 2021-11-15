FROM node:14-alpine AS build
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build --prod
FROM nginx:1.20.1-alpine
COPY --from=build /app/dist/front-biocaptcha /usr/share/nginx/html