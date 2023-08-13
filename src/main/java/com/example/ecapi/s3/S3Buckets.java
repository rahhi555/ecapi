package com.example.ecapi.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {
    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    private String products;
}
