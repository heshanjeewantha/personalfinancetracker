package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.UserDTO;
import com.personalfinancetracker.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);
    User getUserById(String id);
    List<User> getAllUsers();
    User updateUser(String id, UserDTO userDTO);
    void deleteUser(String id);
    User authenticate(String username, String password);
}
