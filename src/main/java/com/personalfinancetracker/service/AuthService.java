package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.LoginRequest;
import com.personalfinancetracker.dto.LoginResponse;
import com.personalfinancetracker.model.User;
import com.personalfinancetracker.repository.UserRepository;
import com.personalfinancetracker.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate token with role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token, "Login successful");
    }

}
