package com.shoplife;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.shoplife.api.RestController;
import com.shoplife.service.ProductManager;
import com.shoplife.storage.ProductsDbConnector;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ApplicationEntryPoint {

    static Logger logger = LoggerFactory.getLogger(ApplicationEntryPoint.class);

    public static void main(String[] args) {

        String environment = System.getenv("APP_ENV"); // Set this variable accordingly

        HashMap<String, String> dbConfig = new HashMap<>();
        dbConfig.put("dbUsername", System.getenv("PSQL_USER"));
        dbConfig.put("dbPassword", System.getenv("PSQL_PASSWORD"));
        dbConfig.put("psqlHost", System.getenv("PSQL_HOST"));
        dbConfig.put("psqlPort", System.getenv("PSQL_PORT"));
        dbConfig.put("psqlSchema", System.getenv("PSQL_SCHEMA"));
        dbConfig.put("psqlDatabase", System.getenv("PSQL_DB"));

        /*ALWAYS DO logging FIRST*/
        configureLogging(environment);
        initializeComponents(environment, dbConfig);

        logger.info("Starting products-api");
    }

    private static void initializeComponents(String environment, HashMap<String, String> dbConfig) {
        ProductsDbConnector dbConnector = new ProductsDbConnector(dbConfig);
        ProductManager productManager = new ProductManager(dbConnector);

        /*
         * Does not consume from Kafka in local environment
         */


        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("consumer.group.id", "products-consumer");
        kafkaConfig.put("key.deserializer", StringDeserializer.class.getName());
        kafkaConfig.put("value.deserializer", StringDeserializer.class.getName());
        kafkaConfig.put("topic", "products.v0");

        if ("docker".equalsIgnoreCase(environment))
            kafkaConfig.put("bootstrap.servers", "broker:29092");
        else
            /*Still will not work because in the docker compose the kafka can be configured to either use localhost
            on the host or the docker network.
            use network-mode: host in the docker compose file to use localhost on the host or use networks: network to
            use it withing the docker network
            */
            kafkaConfig.put("bootstrap.servers", "localhost:29092");

        new ProductsKafkaConsumer(productManager, kafkaConfig).startConsumption();
        new RestController(productManager).listen();
    }

    private static void configureLogging(String environment) {
        String logFile;
        if ("docker".equalsIgnoreCase(environment)) {
            System.setProperty("logback.configurationFile", "logback.xml");
        } else {
            logFile = "logback-host.xml";
            System.setProperty("logback.configurationFile", logFile);

            try (InputStream resourceAsStream = ApplicationEntryPoint.class.getClassLoader().getResourceAsStream(logFile);) {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                JoranConfigurator joranConfigurator = new JoranConfigurator();

                joranConfigurator.setContext(loggerContext);
                joranConfigurator.doConfigure(resourceAsStream);
            } catch (Exception e) {
                logger.error("Error configuring logging", e);
            }
        }
    }
}