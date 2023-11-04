const http = require('http');
const { Kafka } = require('kafkajs');


const kafka = new Kafka({
    clientId: 'user-service',
    brokers: ['broker:29092']  // Replace with your Kafka broker address
});

const producer = kafka.producer();

const server = http.createServer((req, res) => {
    producer.connect().then(() => {
        producer.send({
            topic: 'products.v0',
            messages: [
                { value: JSON.stringify({ productName: 'ProductOne', price: '20.5' }) },
            ]
        }).then(() => {
            console.log('Message sent successfully!');
        }).catch((error) => {
            console.error('Error in sending message:', error);
            req.end('Error in sending message');
        });
    });

    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end('publishing message to kafka');
});



server.listen(3000);