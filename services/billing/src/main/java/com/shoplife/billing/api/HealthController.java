package com.shoplife.billing.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HealthController {

    public static final Logger LOGGER = Logger.getLogger("HealthController");

    @GetMapping("/health")
    public String health() {
        LOGGER.info("Health check");
        return "OK";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        LOGGER.log(Level.WARNING, e, () -> "Returning HTTP 400 Bad Request");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle(Exception e) {
        LOGGER.log(Level.WARNING, e, () -> "Returning HTTP 404 Bad Request");
    }
}
