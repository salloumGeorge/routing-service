package com.shoplife.productsmanager;

import com.shoplife.productsmanager.api.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;


    public void sendProductMessage(Product message) {
        kafkaTemplate.send(kafkaConfig.getTopic(), message.asJson());

    }
}
