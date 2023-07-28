package com.example.ecapi.controller;

import com.example.ecapi.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HealthController.class)
@Import(SecurityConfig.class)
public class HealthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void test_healthCheck() throws Exception {
        mockMvc.perform(get("/health")).andExpect(status().isOk());
    }
}
