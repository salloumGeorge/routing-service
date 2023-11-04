package com.shoplife;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoplife.model.ProductInput;
import com.shoplife.service.ProductManager;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsKafkaConsumer {

    public static final int MILLIS = 100;
    Logger logger = LoggerFactory.getLogger(ProductsKafkaConsumer.class);

    private ProductManager productManager;
    private String bootstrapServers;
    private String groupId;
    private String valueDeserializer;
    private String keyDeserializer;
    private String topic;

    public ProductsKafkaConsumer(ProductManager productManager, Map<String, String> kafkaConfig) {
        this.productManager = productManager;
        bootstrapServers = kafkaConfig.get("bootstrap.servers");
        groupId = kafkaConfig.get("consumer.group.id");
        keyDeserializer = kafkaConfig.get("key.deserializer");
        valueDeserializer = kafkaConfig.get("value.deserializer");
        topic = kafkaConfig.get("topic");
    }

    public void startConsumption() {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);


        Consumer<String, String> consumer = new KafkaConsumer<>(props);

        List<PartitionInfo> partitions = consumer.partitionsFor(topic);

        ExecutorService executor = Executors.newFixedThreadPool(partitions.size());

        for (PartitionInfo partition : partitions) {
            int partitionNumber = partition.partition();
            KafkaConsumer<String, String> partitionConsumer = new KafkaConsumer<>(props);

            // Create a topic partition assignment
            TopicPartition topicPartition = new TopicPartition(topic, partitionNumber);
            partitionConsumer.assign(Collections.singletonList(topicPartition));

            // Start a new thread for this partition's consumer
            executor.execute(() -> {
                do {
                    ConsumerRecords<String, String> records = partitionConsumer.poll(Duration.ofMillis(MILLIS)); // Adjust the poll duration as needed
                    for (ConsumerRecord<String, String> record : records) {
                        String key = record.key();
                        String value = record.value();
                        logger.info("Received message from partition " + record.partition() + ": key = " + key + ", value = " + value);
                        try {
                            ProductInput productInput = new ObjectMapper().readValue(record.value(), ProductInput.class);
                            productManager.saveProduct(productInput);
                        } catch (JsonProcessingException e) {
                            logger.error("Failed to parse message", e);
                        } catch (SQLException e) {
                            logger.error("Failed to save product", e);
                        }
                    }
                } while (true);
            });
        }
    }
}
