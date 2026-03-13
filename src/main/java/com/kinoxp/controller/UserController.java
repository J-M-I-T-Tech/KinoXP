package com.kinoxp.controller;

import com.kinoxp.dto.LoginRequest;
import com.kinoxp.dto.UserRegistrationRequest;
import com.kinoxp.model.user.User;
import com.kinoxp.security.AdminChecker;
import com.kinoxp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kino/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Hent alle brugere
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Opret bruger
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.name(), request.password());

        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Slet bruger
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @RequestParam Long adminUserId) {
        if (!AdminChecker.isAdmin(userService, adminUserId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean deleted = userService.deleteUserById(userId);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}
