//package com.kinoxp.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    private UserRegistrationRequest validRequest;
//
//    @BeforeEach
//    void setUp() {
//        validRequest = new UserRegistrationRequest(
//                "Alice",
//                LocalDate.now().minusYears(20),
//                Role.CUSTOMER,
//                "password123"
//        );
//    }
//
//    @Test
//    void createUser_shouldSaveAndReturnUser_whenUserIsOldEnough() {
//        User savedUser = new User();
//        savedUser.setName(validRequest.name());
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        User result = userService.createUser(validRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getName()).isEqualTo("Alice");
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void createUser_shouldThrowException_whenUserIsUnder13() {
//        UserRegistrationRequest tooYoung = new UserRegistrationRequest(
//                "Bob", LocalDate.now().minusYears(10), Role.CUSTOMER, "pass"
//        );
//
//        assertThatThrownBy(() -> userService.createUser(tooYoung))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Bruger skal være mindst 13 år gammel");
//
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void createUser_shouldThrowException_whenUserIsExactly12() {
//        UserRegistrationRequest exactly12 = new UserRegistrationRequest(
//                "Charlie", LocalDate.now().minusYears(12).plusDays(1), Role.CUSTOMER, "pass"
//        );
//
//        assertThatThrownBy(() -> userService.createUser(exactly12))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void createUser_shouldSucceed_whenUserIsExactly13() {
//        UserRegistrationRequest exactly13 = new UserRegistrationRequest(
//                "Dana", LocalDate.now().minusYears(13), Role.CUSTOMER, "pass"
//        );
//        when(userRepository.save(any(User.class))).thenReturn(new User());
//
//        assertThatNoException().isThrownBy(() -> userService.createUser(exactly13));
//    }
//
//    @Test
//    void login_shouldReturnUser_whenCredentialsAreCorrect() {
//        User user = new User();
//        user.setName("Alice");
//        when(userRepository.findByNameAndPassword("Alice", "password123")).thenReturn(Optional.of(user));
//
//        User result = userService.login("Alice", "password123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getName()).isEqualTo("Alice");
//    }
//
//    @Test
//    void login_shouldReturnNull_whenCredentialsAreWrong() {
//        when(userRepository.findByNameAndPassword("Alice", "wrongpass")).thenReturn(Optional.empty());
//
//        assertThat(userService.login("Alice", "wrongpass")).isNull();
//    }
//
//    @Test
//    void getAllUsers_shouldReturnAllUsers() {
//        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
//
//        assertThat(userService.getAllUsers()).hasSize(2);
//    }
//
//    @Test
//    void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() {
//        when(userRepository.findAll()).thenReturn(List.of());
//
//        assertThat(userService.getAllUsers()).isEmpty();
//    }
//
//    @Test
//    void deleteUserById_shouldReturnTrue_whenUserExists() {
//        when(userRepository.existsById(1L)).thenReturn(true);
//
//        assertThat(userService.deleteUserById(1L)).isTrue();
//        verify(userRepository).deleteById(1L);
//    }
//
//    @Test
//    void deleteUserById_shouldReturnFalse_whenUserDoesNotExist() {
//        when(userRepository.existsById(99L)).thenReturn(false);
//
//        assertThat(userService.deleteUserById(99L)).isFalse();
//        verify(userRepository, never()).deleteById(anyLong());
//    }
//
//    @Test
//    void findById_shouldReturnUser_whenUserExists() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
//
//        assertThat(userService.findById(1L)).isNotNull();
//    }
//
//    @Test
//    void findById_shouldReturnNull_whenUserDoesNotExist() {
//        when(userRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThat(userService.findById(99L)).isNull();
//    }
//
//    @Test
//    void isAdmin_shouldReturnTrue_whenUserHasAdminRole() {
//        User admin = new User();
//        admin.setRole(Role.ADMIN);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
//
//        assertThat(userService.isAdmin(1L)).isTrue();
//    }
//
//    @Test
//    void isAdmin_shouldReturnFalse_whenUserHasUserRole() {
//        User regularUser = new User();
//        regularUser.setRole(Role.CUSTOMER);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(regularUser));
//
//        assertThat(userService.isAdmin(1L)).isFalse();
//    }
//
//    @Test
//    void isAdmin_shouldReturnFalse_whenUserDoesNotExist() {
//        when(userRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThat(userService.isAdmin(99L)).isFalse();
//    }
//}
