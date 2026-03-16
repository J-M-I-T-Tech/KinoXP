package com.kinoxp.service;

import com.kinoxp.dto.UserRegistrationRequest;
import com.kinoxp.model.user.Role;
import com.kinoxp.model.user.User;
import com.kinoxp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserRegistrationRequest(
                "Alice",
                LocalDate.now().minusYears(20),
                Role.CUSTOMER,
                "password123"
        );
    }

    @Test
    void createUser_shouldSaveAndReturnUser_whenUserIsOldEnough() {
        // Arrange
        User savedUser = new User();
        savedUser.setName(validRequest.name());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenUserIsUnder13() {
        // Arrange
        UserRegistrationRequest tooYoung = new UserRegistrationRequest(
                "Bob",
                LocalDate.now().minusYears(10),
                Role.CUSTOMER,
                "pass"
        );

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(tooYoung))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bruger skal være mindst 13 år gammel");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowException_whenUserIsExactly12() {
        // Arrange
        UserRegistrationRequest exactly12 = new UserRegistrationRequest(
                "Charlie",
                LocalDate.now().minusYears(12).plusDays(1),
                Role.CUSTOMER,
                "pass"
        );

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(exactly12))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createUser_shouldSucceed_whenUserIsExactly13() {
        // Arrange
        UserRegistrationRequest exactly13 = new UserRegistrationRequest(
                "Dana",
                LocalDate.now().minusYears(13),
                Role.CUSTOMER,
                "pass"
        );
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act & Assert
        assertThatNoException().isThrownBy(() -> userService.createUser(exactly13));
    }

    @Test
    void login_shouldReturnUser_whenCredentialsAreCorrect() {
        // Arrange
        User user = new User();
        user.setName("Alice");
        when(userRepository.findByNameAndPassword("Alice", "password123"))
                .thenReturn(Optional.of(user));

        // Act
        User result = userService.login("Alice", "password123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alice");
    }

    @Test
    void login_shouldReturnNull_whenCredentialsAreWrong() {
        // Arrange
        when(userRepository.findByNameAndPassword("Alice", "wrongpass"))
                .thenReturn(Optional.empty());

        // Act
        User result = userService.login("Alice", "wrongpass");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // Arrange
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void deleteUserById_shouldReturnTrue_whenUserExists() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = userService.deleteUserById(1L);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserById_shouldReturnFalse_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = userService.deleteUserById(99L);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Arrange
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        User result = userService.findById(99L);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void isAdmin_shouldReturnTrue_whenUserHasAdminRole() {
        // Arrange
        User admin = new User();
        admin.setRole(Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        // Act & Assert
        assertThat(userService.isAdmin(1L)).isTrue();
    }

    @Test
    void isAdmin_shouldReturnFalse_whenUserHasUserRole() {
        // Arrange
        User regularUser = new User();
        regularUser.setRole(Role.CUSTOMER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(regularUser));

        // Act & Assert
        assertThat(userService.isAdmin(1L)).isFalse();
    }

    @Test
    void isAdmin_shouldReturnFalse_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThat(userService.isAdmin(99L)).isFalse();
    }
}