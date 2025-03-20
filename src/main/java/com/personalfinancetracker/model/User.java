package com.personalfinancetracker.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password; // Encrypted password

    // private String preferredCurrency;

    @NotNull(message = "Role cannot be null")
    @Size(min = 4, max = 5, message = "Role must be either 'ADMIN' or 'USER'")
    private String role; // ADMIN or USER

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Goal Tracking Integration
    // private List<String> goalIds; // Stores IDs of linked goals
}
