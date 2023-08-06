package com.pacifique.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user_one;
    private User user_two;

    @BeforeEach
    void setUp() {
        user_one =
            User
                .builder()
                .id(1L)
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
        user_two =
            User
                .builder()
                .id(2L)
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Testing Register a user")
    void testRegisterUser() {
        // arrange
        when(userRepository.save(any(User.class))).thenReturn(user_one);

        //act
        Long registered = userService.registerUser(
            new UserRequest("peter", "peter@gmail.com", "user")
        );

        //Assert
        assertEquals(registered, 1L);
    }

    @Test
    @DisplayName("Testing List retrieve List of user")
    void testListUser() {
        // arrange
        when(userRepository.findAll()).thenReturn(List.of(user_one, user_two));
        var expected_users = Stream
            .of(user_one, user_two)
            .map(
                user ->
                    UserResponse
                        .builder()
                        .id(user.getId())
                        .role(user.getRole())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .createAt(user.getCreatedAt())
                        .build()
            )
            .toList();

        //act
        List<UserResponse> allUsers = userService.allUsers();

        //Assert
        assertEquals(allUsers, expected_users);
    }

    @Test
    @DisplayName("Testing retrieve use by Id")
    void testRetrieveUserById() {
        //arrange
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.ofNullable(user_one));
        var expected_user = UserResponse
            .builder()
            .id(user_one.getId())
            .email(user_one.getEmail())
            .role(user_one.getRole())
            .fullName(user_one.getFullName())
            .createAt(user_one.getCreatedAt())
            .build();

        //Act
        var user = userService.getUser(1L);

        //assert
        assertEquals(user, expected_user);
    }
}