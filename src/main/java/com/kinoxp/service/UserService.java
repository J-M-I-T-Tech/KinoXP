package com.kinoxp.service;

import com.kinoxp.dto.UserRegistrationRequest;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setDateOfBirth(request.dateOfBirth());
        user.setRole(request.role());
        user.setPassword(request.password());
        return userRepository.save(user);
    }

    public User login(String name, String password) {
        Optional<User> user = userRepository.findByNameAndPassword(name, password);
        return user.orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserById(long userId) {
        if (!userRepository.existsById(userId)) return false;

        userRepository.deleteById(userId);
        return true;
    }
}
