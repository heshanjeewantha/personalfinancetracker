package com.personalfinancetracker.service;

import com.personalfinancetracker.controller.AuthController;
import com.personalfinancetracker.dto.LoginRequest;
import com.personalfinancetracker.dto.LoginResponse;
import com.personalfinancetracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        LoginResponse loginResponse = new LoginResponse("sampleToken", "Login successful");
        when(authService.login(loginRequest)).thenReturn(loginResponse);

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("sampleToken", response.getBody().getToken());
        assertEquals("Login successful", response.getBody().getMessage());
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("wrong@example.com", "wrongpassword");
        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
    }
}
