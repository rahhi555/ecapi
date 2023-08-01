package com.example.ecapi.service.product;

import com.example.ecapi.model.DTOProduct;
import com.example.ecapi.model.FormProduct;
import com.example.ecapi.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public DTOProduct addProduct(FormProduct formProduct) {
        return repository.insert(formProduct);
    }

    public List<DTOProduct> getProducts() {
        return repository.selectList();
    }
}
