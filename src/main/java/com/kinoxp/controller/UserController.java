package com.kinoxp.controller;

import com.kinoxp.dto.LoginRequest;
import com.kinoxp.dto.UserRegistrationRequest;
import com.kinoxp.model.user.User;
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
    public User createUser(@RequestBody UserRegistrationRequest request) {
        return userService.createUser(request);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        User user = userService.login(request.name(), request.password());

        if(user != null) {
            return ResponseEntity.ok("Log ind er succesfuldt");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Forkert navn eller password");
    }

    // Slet bruger
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("Bruger med id " + id + " er blevet slettet.");
    }
}
