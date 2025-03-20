package com.personalfinancetracker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/users/**").permitAll() // Public routes
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")           // Only ADMIN
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/auth/**", "/api/currency/**").permitAll()
                        .requestMatchers("/api/currency/**").permitAll()
                        .requestMatchers("/api/goals/**").authenticated() // Requires authentication for goal tracking
                        .requestMatchers("/api/transactions/**").hasAnyRole("USER", "ADMIN")// USER & ADMIN
                        .requestMatchers("/api/notifications/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());  // For basic auth testing (optional)

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
