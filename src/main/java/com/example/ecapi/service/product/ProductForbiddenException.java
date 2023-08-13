package com.example.ecapi.service.product;

public class ProductForbiddenException extends RuntimeException {
    public ProductForbiddenException(Integer id) {
        super("Product " + id + " is forbidden");
    }
}
