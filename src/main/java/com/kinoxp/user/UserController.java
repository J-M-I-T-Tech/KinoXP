package com.kinoxp.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kino/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Ikke logget ind");
        }

        User user = userService.findByName(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(404).body("Bruger ikke fundet");
        }

        return ResponseEntity.ok(new UserSessionResponse(user.getUserId(), user.getName(), user.getRole()));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
//        User user = userService.login(request.name(), request.password());
//        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return userService.deleteUserById(userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    record UserSessionResponse(Long userId, String name, Role role) {}
}
