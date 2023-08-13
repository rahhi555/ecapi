package com.example.ecapi.service.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Integer productId) {
        super("product (id = " + productId + ") is not found.");
    }
}
