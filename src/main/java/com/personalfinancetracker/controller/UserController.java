package com.personalfinancetracker.controller;

import com.personalfinancetracker.dto.UserDTO;
import com.personalfinancetracker.model.User;
import com.personalfinancetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<User> createUser(@Valid  @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }


    @RestController
    @RequestMapping("/api/user")  //role base authorization
    public class useController {

        @GetMapping("/dashboard")
        public ResponseEntity<String> userDashboard() {
            return ResponseEntity.ok("Welcome to userDashboard");
        }
    }

    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {

        @GetMapping("/dashboard")
        public ResponseEntity<String> adminDashboard() {
            return ResponseEntity.ok("Welcome to ADMIN Dashboard");
        }
    }
}
