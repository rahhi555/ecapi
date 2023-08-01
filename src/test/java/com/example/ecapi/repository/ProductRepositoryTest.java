package com.example.ecapi.repository;

import static org.assertj.core.api.Assertions.*;

import com.example.ecapi.model.DTOProduct;
import com.example.ecapi.model.FormProduct;
import com.example.ecapi.repository.product.ProductRepository;
import com.example.ecapi.security.User;
import com.example.ecapi.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Sql("/common/VendorInsertOne.sql")
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void addProduct() {
        User user = userRepository.findByEmail("test@example.com").orElseThrow();
        FormProduct formProduct = new FormProduct(user.getId(), "Tシャツ", 1000, 3);
        formProduct.setDescription("シンプルでかっこいいTシャツです");
        formProduct.setThumbnailUrl("https://example.com");
        formProduct.setCarouselUrls(List.of("https://example.com", "https://example.com"));
        DTOProduct product = productRepository.insert(formProduct);
        assertThat(product.getName()).isEqualTo("Tシャツ");
    }

    @Test
    void getProducts() {
        User user = userRepository.findByEmail("test@example.com").orElseThrow();
        FormProduct formProduct1 = new FormProduct(user.getId(), "Tシャツ", 1000, 3);
        FormProduct formProduct2 = new FormProduct(user.getId(), "カップラーメン", 500, 30);
        FormProduct formProduct3 = new FormProduct(user.getId(), "M2 Mac Pro", 500000, 1);
        productRepository.insert(formProduct1);
        productRepository.insert(formProduct2);
        productRepository.insert(formProduct3);

        List<DTOProduct> products = productRepository.selectList();
        assertThat(products.size()).isEqualTo(3);
    }
}
