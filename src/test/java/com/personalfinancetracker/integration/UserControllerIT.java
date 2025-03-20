package com.personalfinancetracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalfinancetracker.dto.UserDTO;
import com.personalfinancetracker.model.User;
import com.personalfinancetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Clear all users before each test
    }

    /**  Test: Create a new user */
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"}) // Mock user authentication
    void testCreateUser() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .username("JohnDoe")
                .email("johndoe@example.com")
                .password("password123")
                .role("USER")
                .build();

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    /** Test: Get a user by ID */
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        User user = User.builder()
                .username("JaneDoe")
                .email("janedoe@example.com")
                .password("password123")
                .role("USER")
                .build();

        user = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JaneDoe"))
                .andExpect(jsonPath("$.email").value("janedoe@example.com"));
    }

    /** Test: Update a user's details */
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testUpdateUser() throws Exception {
        User user = User.builder()
                .username("OldName")
                .email("oldname@example.com")
                .password("password123")
                .role("USER")
                .build();

        user = userRepository.save(user);

        UserDTO updatedUser = UserDTO.builder()
                .username("NewName")
                .email("newname@example.com")
                .password("newpassword")
                .role("USER")
                .build();

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("NewName"))
                .andExpect(jsonPath("$.email").value("newname@example.com"));
    }

    /**  Test: Delete a user */
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        User user = User.builder()
                .username("DeleteUser")
                .email("deleteuser@example.com")
                .password("password123")
                .role("USER")
                .build();

        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    /** Test: Get all users */
    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        User user1 = User.builder().username("Alice").email("alice@example.com").password("pass").role("USER").build();
        User user2 = User.builder().username("Bob").email("bob@example.com").password("pass").role("USER").build();

        userRepository.saveAll(List.of(user1, user2));

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
