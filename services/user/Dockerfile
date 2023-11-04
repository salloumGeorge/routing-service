# use the official lightweight Node.js image
FROM node:alpine

# set working directory inside the container
WORKDIR '/app'

# copy package.json and package-lock.json to /app
COPY package*.json ./
COPY user-app.js .

# Install application dependencies
RUN npm install

# Install curl for debugging
RUN apk update && apk add --no-cache curl

# expose port 3000
EXPOSE 3000

# start the application
CMD ["node", "user-app.js"]

