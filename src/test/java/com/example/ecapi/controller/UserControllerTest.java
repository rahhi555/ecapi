package com.example.ecapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecapi.security.repository.UserRepository;
import com.example.ecapi.security.service.AuthenticationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import utils.UserInsertOne;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Test
    void test_getUser() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        mockMvc.perform(
                get("/users/{id}", user.user().getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.token()))
                .andExpect(status().isOk());
    }

    @Test
    void test_getUser_Bearer無しは403エラーが返ってくること() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        mockMvc.perform(get("/users/{id}", user.user().getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_me() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        mockMvc.perform(
                        get("/users/me")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.user().getEmail()))
                .andExpect(jsonPath("$.role").value(user.user().getRole().name()));
    }

    @Test
    void test_me_Bearer無しは403エラーが返ってくること() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }
}
