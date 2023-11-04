package com.shoplife.service;

import com.shoplife.model.Product;
import com.shoplife.model.ProductInput;
import com.shoplife.storage.ProductsDbConnector;

import java.sql.SQLException;
import java.util.Optional;

public class ProductManager {

    ProductsDbConnector dbConnector;
    public ProductManager(ProductsDbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public Optional<Product> getProduct(int id) throws SQLException {
        return dbConnector.getProduct(id);
    }

    public void saveProduct(ProductInput product) throws SQLException {
        dbConnector.saveProduct(product);
    }

}
