package com.personalfinancetracker.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalfinancetracker.controller.UserController;
import com.personalfinancetracker.dto.UserDTO;
import com.personalfinancetracker.model.User;
import com.personalfinancetracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = User.builder()
                .id("1")
                .username("John Doe")
                .email("johndoe@example.com")
                .password("password123")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO("John Doe", "johndoe@example.com", "password123", "USER");

        when(userService.createUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById("1")).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));

        verify(userService, times(1)).getUserById("1");
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(user, user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO("Updated User", "updated@example.com", "newpassword", "USER");

        User updatedUser = User.builder()
                .id("1")
                .username("Updated User")
                .email("updated@example.com")
                .password("newpassword")
                .role("USER")
                .createdAt(user.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.updateUser(eq("1"), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService, times(1)).updateUser(eq("1"), any(UserDTO.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService, times(1)).deleteUser("1");
    }
}

