package com.example.ecapi.service.product;

import com.example.ecapi.model.DTOProduct;
import com.example.ecapi.model.FormProduct;
import com.example.ecapi.repository.product.ProductRepository;
import com.example.ecapi.s3.S3Buckets;
import com.example.ecapi.s3.S3Util;
import com.example.ecapi.security.User;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final S3Util s3Util;
    private final S3Buckets s3Buckets;

    public ProductService(ProductRepository repository, S3Util s3Util, S3Buckets s3Buckets) {
        this.repository = repository;
        this.s3Util = s3Util;
        this.s3Buckets = s3Buckets;
    }

    public DTOProduct addProduct(FormProduct formProduct) {
        return repository.insert(formProduct);
    }

    public DTOProduct addThumbnail(Integer id, MultipartFile file) {
        var product = repository.select(id).orElseThrow(() -> new ProductNotFoundException(id));
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!product.getVendorId().equals(user.getId())) {
            throw new ProductForbiddenException(id);
        }
        String path = null;
        try {
            path = s3Util.putObject(s3Buckets.getProducts(), file.getOriginalFilename(), file.getBytes());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return repository.updateThumbnail(id, path);
    }

    public List<DTOProduct> getProducts() {
        return repository.selectList();
    }
}
