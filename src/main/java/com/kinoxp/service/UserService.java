package com.kinoxp.service;

import com.kinoxp.dto.UserRegistrationRequest;
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.UserRepository;
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
        if(!sOldEnough(request.dateOfBirth())){
            throw new IllegalArgumentException("Bruger skal være mindst 13 år gammel");
        }
        User user = new User();
        user.setName(request.name());
        user.setDateOfBirth(request.dateOfBirth());
        user.setRole(request.role());
        user.setPassword(request.password());
        return userRepository.save(user);
    }

    private boolean sOldEnough(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 13;
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

    public User findById(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }
}
