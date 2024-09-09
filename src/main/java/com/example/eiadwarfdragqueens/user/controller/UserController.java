package com.example.eiadwarfdragqueens.user.controller;

import com.example.eiadwarfdragqueens.portfolio.modelEntity.Portfolio;
import com.example.eiadwarfdragqueens.user.config.JwtService;
import com.example.eiadwarfdragqueens.user.modelEntity.AuthRequest;
import com.example.eiadwarfdragqueens.user.modelEntity.Role;
import com.example.eiadwarfdragqueens.user.modelEntity.User;
import com.example.eiadwarfdragqueens.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Authentication and JWT endpoints

    @GetMapping("/auth/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure";
    }

    @PostMapping("/auth/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/auth/getUserData")
    public String getUserData(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtService.extractUsername(token);
    }

    // User management endpoints

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{typeId}/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String typeId, @PathVariable Long id) {
        Optional<User> user = userService.findUserById(typeId, id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            User savedUser = userService.addUser(user);
            response.put("message", "User created successfully");
            response.put("userId", savedUser.getTypeId() + "-" + savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/users/{typeId}/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String typeId, @PathVariable Long id, @Valid @RequestBody User userDetails) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<User> user = userService.findUserById(typeId, id);
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setName(userDetails.getName());
                existingUser.setEmail(userDetails.getEmail());
                existingUser.setPassword(userDetails.getPassword());
                existingUser.setRole(userDetails.getRole());
                userService.addUser(existingUser);
                return ResponseEntity.ok(existingUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/users/{typeId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUser(@PathVariable String typeId, @PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUser(typeId, id);
            response.put("typeId", typeId);
            response.put("id", String.valueOf(id));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            response.put("message", "Credentials invalid");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public void createUserWithPortfolio(User user, Portfolio portfolio) {
        if (user.getRole() != Role.MODEL) {
            throw new IllegalArgumentException("Only users with role MODEL can have a portfolio.");
        }
        user.setPortfolio(portfolio);
        portfolio.setUser(user);
        userService.addUser(user);
    }

}
