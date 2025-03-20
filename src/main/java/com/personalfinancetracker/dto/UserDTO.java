package com.personalfinancetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {    //user dto
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    @NotBlank(message = "Role cannot be empty")
    private String role; // ADMIN or USER
   // private String preferredCurrency;
}
