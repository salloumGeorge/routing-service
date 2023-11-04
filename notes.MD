## Service Definition

```yaml
services:
    service1_instance1:
        image: node:alpine  
        working_dir: /app
        volumes:
            - ./service1_instance1:/app
    command: [ "node", "app.js"]  # Replace with your Node.js script
    networks:
        - app-network
      
```
In the example above, instead of having a separate dockerfile for the service, we can use the image node:alpine. This image is a lightweight version of node. We can then specify the working directory and the command to run the node script.
The volume is used to mount the local directory to the container. This allows us to make changes to the code and see the changes reflected in the container.



## Docker Compose Commands
```bash
docker-compose up
```

## Environment Variables
Can be passed through a file definition
```yaml
product_instance1:
    build:
      context: ./services/product
    env_file:
      - .env # This file contains the environment variables 
    ports:
      - "4001:4000"
    networks:
      - app-network
```

## Kafka 
### How do clients connect to Kafka?
The client connects to one broker but can discover the other brokers in the cluster.

#### Listener:

* Kafka broker is configured to listen on specific network interfaces and ports.
* When a Kafka client (producer, consumer, etc.) wants to connect to the broker, it specifies which listener it wants to use (based on the client's configuration).
* The client initiates a connection to the broker's listener using the specified protocol (PLAINTEXT, SSL, etc.).
* The broker's listener accepts the incoming connection request from the client. 

#### Advertised Listener:

* After the connection is established, the Kafka broker responds to the client with information about the advertised listener.
* This advertised listener address is used by the client for future communication with the broker.
* The advertised listener ensures that the client knows how to reach the broker, even if the client's perspective (inside or outside a network) is different from the broker's listener configuration.
