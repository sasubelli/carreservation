package com.ss.carreservation.controller;

import com.ss.carreservation.dto.UserDTO;
import com.ss.carreservation.entity.Customer;
import com.ss.carreservation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor injection for the UserService interface
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new Customer.
     * Since Customer is a subclass of User, the Service handles the inheritance logic.
     */
    @PostMapping("/register/customer")
    public ResponseEntity<UserDTO> registerCustomer(@RequestBody Customer customer) {
        UserDTO createdUser = userService.registerUser(customer);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Fetch a specific user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieve all users as DTOs (Safe for public consumption).
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Delete a user by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}