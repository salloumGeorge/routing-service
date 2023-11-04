package com.shoplife.api;

import com.shoplife.model.Product;
import com.shoplife.service.ProductManager;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestController {
    public static final int BACKLOG = 20;
    public static final int N_THREADS = 5;
    public final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(N_THREADS);
    Logger logger = org.slf4j.LoggerFactory.getLogger(RestController.class);

    private ProductManager productManager;

    public RestController(ProductManager productManager) {
        this.productManager = productManager;
    }

    /*Health check API*/
    public void listen() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8088), BACKLOG);
            server.createContext("/api/inventory/health", healthHandler());
            server.createContext("/api/inventory/products", new ProductsHandler(productManager));
            server.setExecutor(THREAD_POOL);
            server.start();
        } catch (IOException e) {
            logger.error("Error handling requests", e);
        }
    }


    private static HttpHandler healthHandler() {
        return exchange -> {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();
            exchange.close();
        };
    }
}

/*
 * /api/products/{id}
 * */
class ProductsHandler implements HttpHandler {
    private final ProductManager pm;

    public ProductsHandler(ProductManager productManager) {
        this.pm = productManager;
    }

    @Override
    public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String[] pathParts = requestURI.getPath().split("/");

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=UTF-8");
        OutputStream output = exchange.getResponseBody();

        if (pathParts.length > 5) {
            String response = "{\"error\": \"Bad Request\"}";
            exchange.sendResponseHeaders(404, response.length());
            exchange.getResponseBody().write(response.getBytes());
        } else {
            String productId = pathParts[4];
            try {
                int productIdInt = Integer.parseInt(productId);
                Optional<Product> product = pm.getProduct(productIdInt);
                if (product.isEmpty()) {
                    String response = "{\"error\": \"Product not found\"}";
                    exchange.sendResponseHeaders(404, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    output.flush();
                    exchange.close();
                }

                String response = "{\"id\": " + product.get().id() + ", \"name\": \"" + product.get().name() + "\", \"price\": " + product.get().price() + "}";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                output.write(response.getBytes());

            } catch (NumberFormatException e) {
                String response = "{\"error\": \"Bad Request. Id is an integer\"}";
                exchange.sendResponseHeaders(400, response.length());
                exchange.getResponseBody().write(response.getBytes());
                output.flush();
                exchange.close();
            } catch (SQLException e) {
                String response = "{\"error\": \"Internal Server Error\"}";
                exchange.sendResponseHeaders(500, response.length());
                exchange.getResponseBody().write(response.getBytes());
                output.flush();
                exchange.close();
            }
        }
        output.flush();
        exchange.close();

    }
}
