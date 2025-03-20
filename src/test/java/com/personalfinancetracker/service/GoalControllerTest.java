package com.personalfinancetracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalfinancetracker.dto.GoalDTO;
import com.personalfinancetracker.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = jwtUtil.generateToken("testuser@example.com", "USER"); // Fix: Ensure role is provided
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = {"USER"}) // Fix: Mock user authentication
    void testCreateGoal() throws Exception {
        GoalDTO goalDTO = GoalDTO.builder()
                .goalName("Buy a Car")
                .goalTargetAmount(10000)
                .autoAllocate(true)
                .build();

        mockMvc.perform(post("/api/goals/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)  // Fix: Add token
                        .content(objectMapper.writeValueAsString(goalDTO)))
                .andExpect(status().isOk())  // Fix: Ensure API returns 200 OK
                .andExpect(jsonPath("$.goalName").value("Buy a Car"))
                .andExpect(jsonPath("$.goalTargetAmount").value(10000));
    }
}
