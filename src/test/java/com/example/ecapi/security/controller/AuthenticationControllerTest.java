package com.example.ecapi.security.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

import com.example.ecapi.model.EnumRole;
import com.example.ecapi.model.FormAuthenticate;
import com.example.ecapi.model.FormRegister;
import com.example.ecapi.security.User;
import com.example.ecapi.security.repository.UserRepository;
import com.example.ecapi.security.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import utils.UserInsertOne;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;


    @Test
    void test_POST_auth_register() throws Exception {
        Optional<User> emptyUser = userRepository.findByEmail("new-user@example.com");
        assertThat(emptyUser.isEmpty()).isEqualTo(true);

        FormRegister req = new FormRegister("new-user@example.com", "password", "newFirstName", "newLastName", EnumRole.VENDOR);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(req);
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        Optional<User> user = userRepository.findByEmail("new-user@example.com");
        assertThat(user.isEmpty()).isEqualTo(false);
    }

    @Test
    void test_POST_auth_authenticate() throws Exception {
        var user = UserInsertOne.createVendor(authenticationService, userRepository);

        FormAuthenticate req = new FormAuthenticate(user.user().getEmail(), user.rawPassword(), user.user().getRole());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(req);

        mockMvc.perform(
                        post("/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user").isNotEmpty());
    }
}
