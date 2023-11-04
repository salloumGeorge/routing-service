package com.shoplife.productsmanager.api;

import com.shoplife.productsmanager.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
public class ProductAddController {
    private final KafkaPublisher shopKafkaPublisher;

    public static final Logger LOGGER = Logger.getLogger("ProductAddController");

    @PostMapping("/products")
    public String add(@RequestBody Product product) {
        LOGGER.info("adding product " + product);
        shopKafkaPublisher.sendProductMessage(product);
        return "OK";
    }
}
