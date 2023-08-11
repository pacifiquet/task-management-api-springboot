package com.pacifique.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.pacifique.todoapp.config.extension.CustomTestExecutionExtension;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Time;
import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.repository.UserRepository;
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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(
    {
        MockTimeExtension.class,
        MockitoExtension.class,
        CustomTestExecutionExtension.class,
    }
)
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
                .createdAt(Time.currentDateTime())
                .build();
        user_two =
            User
                .builder()
                .id(2L)
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();
    }

    @Test
    @DisplayName("Testing Registering a user")
    void testRegisterUser() {
        // arrange
        var expected_response = 1L;
        when(userRepository.save(any(User.class))).thenReturn(user_one);

        //act
        Long actual_response = userService.registerUser(
            new UserRequest("peter", "peter@gmail.com", "user")
        );

        //Assert
        assertEquals(expected_response, actual_response);
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
        List<UserResponse> actual_users = userService.allUsers();

        //Assert
        assertEquals(expected_users, actual_users);
    }

    @Test
    @DisplayName("Testing retrieve user by Id")
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
        var actual_user = userService.getUser(1L);

        //assert
        assertEquals(expected_user, actual_user);
    }
}
