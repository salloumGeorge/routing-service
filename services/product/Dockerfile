FROM node:alpine

WORKDIR '/app'

COPY package*.json ./

COPY products-app.js .

# Install application dependencies
RUN npm install

# Install curl
RUN apk update && apk add --no-cache curl

EXPOSE 4000

CMD ["node", "products-app.js"]

