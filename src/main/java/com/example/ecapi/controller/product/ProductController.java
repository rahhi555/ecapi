package com.example.ecapi.controller.product;

import com.example.ecapi.controller.ProductsApi;
import com.example.ecapi.model.DTOProduct;
import com.example.ecapi.model.FormProduct;
import com.example.ecapi.service.product.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController implements ProductsApi {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<DTOProduct> addProduct(FormProduct formProduct) {
        DTOProduct product = service.addProduct(formProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Override
    public ResponseEntity<List<DTOProduct>> getProducts() {
        List<DTOProduct> products = service.getProducts();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<DTOProduct> addProductThumbnail(Integer id, MultipartFile file) {
        DTOProduct product = service.addThumbnail(id, file);
        return ResponseEntity.ok(product);
    }
}
