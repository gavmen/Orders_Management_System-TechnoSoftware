package com.empresa.logistica.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test for HealthController endpoints.
 * 
 * Uses @WebMvcTest for lightweight testing of web layer only,
 * without loading the full application context.
 */
@WebMvcTest(HealthController.class)
@ActiveProfiles("test")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void health_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/health"))
               .andExpect(status().isOk())
               .andExpect(content().string("Orders Management System is running successfully!"));
    }

    @Test
    void info_ShouldReturnSystemInformation() throws Exception {
        mockMvc.perform(get("/health/info"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Orders Management System"))
               .andExpect(jsonPath("$.version").value("1.0.0"))
               .andExpect(jsonPath("$.description").value("Customer orders with credit limit validation"));
    }
}
