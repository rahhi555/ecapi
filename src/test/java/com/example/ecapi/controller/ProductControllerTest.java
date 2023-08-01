package com.example.ecapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecapi.model.FormProduct;
import com.example.ecapi.security.repository.UserRepository;
import com.example.ecapi.security.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import utils.UserInsertOne;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Test
    void addProduct() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        FormProduct req = new FormProduct(user.user().getId(), "Tシャツ", 1000, 3);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(req);

        mockMvc.perform(
                post("/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendorId").value(user.user().getId()))
                .andExpect(jsonPath("$.name").value("Tシャツ"));
    }
}
