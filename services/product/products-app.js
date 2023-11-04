const http = require('http');
const {Client} = require('pg');

const instanceId = process.env.INSTANCE_ID;
/*
* Only for reading products from the database
* */
const server = http.createServer((req, res) => {
    res.writeHead(200, {'Content-Type': 'text/plain'});

    const dbClient = new Client({
        user: process.env.PSQL_USER,
        host: process.env.PSQL_HOST,
        database: process.env.PSQL_DB,
        password: process.env.PSQL_PASSWORD,
        port: process.env.PSQL_PORT
    });

    const schema = process.env.PSQL_SCHEMA
    const table = 'store_products'
    const query = "SELECT * FROM " + schema + "." + table;

    console.log('Connecting to database...with params ', JSON.stringify(dbClient.connectionParameters)
        + " and schema " + schema
        + " and query " + query);

    dbClient.connect()
        .then(() => {
            const query = 'SELECT * FROM products_schema.store_products';
            return dbClient.query(query);
        })
        .then(result => {
            res.end("Products from instance " + instanceId + "\n" + JSON.stringify(result.rows));
        })
        .catch(error => {
            console.error('Error querying database:', error);
            res.statusCode = 500;
            res.end('Internal Server Error ' + error);
        })
        .finally(() => {
            dbClient.end();
        });

});

server.listen(4000);