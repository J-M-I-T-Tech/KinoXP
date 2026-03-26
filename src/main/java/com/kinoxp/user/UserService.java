package com.kinoxp.user;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRegistrationRequest request) {
        if (!isOldEnough(request.dateOfBirth())) {
            throw new IllegalArgumentException("Bruger skal være mindst 13 år gammel");
        }
        User user = new User();
        user.setName(request.name());
        user.setDateOfBirth(request.dateOfBirth());
        user.setRole(request.role());
        user.setPassword(request.password());
        return userRepository.save(user);
    }

    private boolean isOldEnough(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 13;
    }

    public User login(String name, String password) {
        return userRepository.findByNameAndPassword(name, password).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserById(long userId) {
        if (!userRepository.existsById(userId)) return false;
        userRepository.deleteById(userId);
        return true;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole() == Role.ADMIN;
    }
}
