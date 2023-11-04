package com.shoplife.storage;

import com.shoplife.ApplicationEntryPoint;
import com.shoplife.model.Product;
import com.shoplife.model.ProductInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class ProductsDbConnector {

    static Logger logger = LoggerFactory.getLogger(ApplicationEntryPoint.class);
    private Map<String, String> params;

    public ProductsDbConnector(Map<String,String> params) {
        this.params = params;

        String urlTemplate = "jdbc:postgresql://%s:%s/%s?currentSchema=%s";
        String url = String.format(urlTemplate, params.get("psqlHost"), params.get("psqlPort"), params.get("psqlDatabase"), params.get("psqlSchema"));
        try (Connection connection = DriverManager.getConnection(url, params.get("dbUsername"), params.get("dbPassword"))) {
            logger.info("Connected to the database");
        } catch (SQLException e) {
            logger.error("Failed to connect to the database", e);
        }
    }


    public Optional<Product> getProduct(int id) throws SQLException {
        String urlTemplate = "jdbc:postgresql://%s:%s/%s?currentSchema=%s";
        String url = String.format(urlTemplate, params.get("psqlHost"), params.get("psqlPort"), params.get("psqlDatabase"), params.get("psqlSchema"));

        try (Connection connection = DriverManager.getConnection(url, params.get("dbUsername"), params.get("dbPassword"))) {
            logger.info("Connected to the database");
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM store_products WHERE id = " + id).executeQuery();
            if (resultSet.next()) {
                Product product = new Product(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price"));
                return Optional.of(product);
            }else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Failed to connect to the database", e);
            throw e;
        }
    }

    public void saveProduct(ProductInput product) throws SQLException {
        String urlTemplate = "jdbc:postgresql://%s:%s/%s?currentSchema=%s";
        String url = String.format(urlTemplate, params.get("psqlHost"), params.get("psqlPort"), params.get("psqlDatabase"), params.get("psqlSchema"));
        try (Connection connection = DriverManager.getConnection(url, params.get("dbUsername"), params.get("dbPassword"))) {
            connection.prepareStatement("INSERT INTO store_products (name, price) VALUES ('" + product.name() + "', " + product.price() + ")").execute();
        } catch (SQLException e) {
            logger.error("Failed to connect to the database", e);
            throw e;
        }
    }
}