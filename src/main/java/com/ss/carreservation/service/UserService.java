package com.ss.carreservation.service;

import com.ss.carreservation.dto.UserDTO;
import com.ss.carreservation.entity.User;

import java.util.List;

public interface UserService {
// Contract for registering a generic User (could be Customer or Admin)
UserDTO registerUser(User user);

// Contract for retrieving a user by their ID
UserDTO getUserById(String id);

// Contract for listing all users
List<UserDTO> getAllUsers();

// Contract for deleting a user
void deleteUser(String id);
}
