package com.pacifique.todoapp.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Utils;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.model.VerificationToken;
import com.pacifique.todoapp.repository.UserRepository;
import com.pacifique.todoapp.repository.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith({ MockTimeExtension.class, MockitoExtension.class })
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user_one;
    private User user_two;
    private User verified_user;

    @BeforeEach
    void setUp() {
        user_one =
            User
                .builder()
                .userId(1L)
                .firstName("jack")
                .lastName("peter")
                .email("jack@gmail.com")
                .role("user")
                .password(passwordEncoder.encode("password123"))
                .createdAt(Time.currentDateTime())
                .build();
        user_two =
            User
                .builder()
                .userId(2L)
                .firstName("peter")
                .lastName("jack")
                .email("peter@gmail.com")
                .password(passwordEncoder.encode("password1224"))
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();
        verified_user =
            User
                .builder()
                .userId(3L)
                .firstName("peter")
                .lastName("jack")
                .email("peter@gmail.com")
                .password(passwordEncoder.encode("password1224"))
                .role("user")
                .enabled(true)
                .createdAt(Time.currentDateTime())
                .build();
    }

    @Test
    void testRegisterUser() {
        // arrange
        var expected_response = 1L;
        when(userRepository.save(any(User.class))).thenReturn(user_one);
        when(passwordEncoder.encode(any())).thenReturn("hash_key");
        applicationEventPublisher.publishEvent(any());

        //act
        Long actual_response = userService.registerUser(
            new UserRequest("peter", "jack", "peter@gmail.com", "password123", "user"),
            httpServletRequest
        );

        //Assert
        assertEquals(expected_response, actual_response);
    }

    @Test
    void testListOfUser() {
        // arrange
        when(userRepository.findAll()).thenReturn(List.of(user_one, user_two));
        var expected_users = Stream
            .of(user_one, user_two)
            .map(
                user ->
                    UserResponse
                        .builder()
                        .userId(user.getUserId())
                        .role(user.getRole())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .enabled(user.isEnabled())
                        .createAt(user.getCreatedAt())
                        .build()
            )
            .toList();

        //act
        List<UserResponse> actual_users = userService.listOfUser();

        //Assert
        assertEquals(expected_users, actual_users);
    }

    @ParameterizedTest
    @DisplayName("Testing retrieve user by Id")
    @ValueSource(longs = { 1L, 2L })
    void testGetUser(long userId) {
        //arrange
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.ofNullable(user_one));
        var expected_user = UserResponse
            .builder()
            .userId(user_one.getUserId())
            .email(user_one.getEmail())
            .role(user_one.getRole())
            .firstName(user_one.getFirstName())
            .lastName(user_one.getLastName())
            .enabled(user_one.isEnabled())
            .createAt(user_one.getCreatedAt())
            .build();

        //Act
        var actual_user = userService.getUser(userId);

        //assert
        assertEquals(expected_user, actual_user);
    }

    @ParameterizedTest
    @DisplayName("Testing retrieve user by Invalid Id")
    @ValueSource(longs = 8L)
    void testGetUserWithInvalidId(long invalidId) {
        var expected_message = "user with id: 8 not found";
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> userService.getUser(invalidId))
            .withMessage(expected_message);
    }

    @Test
    void testSaveVerifyToken() {
        VerificationToken expected_object = VerificationToken
            .builder()
            .token(UUID.randomUUID().toString())
            .expirationTime(Utils.calculateTokenExpirationDate())
            .id(1L)
            .user(user_one)
            .build();
        when(verificationTokenRepository.save(any())).thenReturn(expected_object);

        VerificationToken actual_object = verificationTokenRepository.save(
            expected_object
        );

        assertEquals(expected_object, actual_object);
    }

    @Test
    void testVerifyUser() {
        var expected_response = "User verifies Successfully";
        VerificationToken expected_object = VerificationToken
            .builder()
            .token(UUID.randomUUID().toString())
            .expirationTime(Utils.calculateTokenExpirationDate())
            .id(2L)
            .user(user_two)
            .build();
        when(verificationTokenRepository.findByToken(any(String.class)))
            .thenReturn(Optional.ofNullable(expected_object));

        String actual_response = userService.verifyUser(expected_object.getToken());

        assertEquals(expected_response, actual_response);
    }

    @Test
    void testVerifyUserAlreadyVerified() {
        // arrange
        var expected_response = "user is verified";
        VerificationToken expected_object = VerificationToken
            .builder()
            .token(UUID.randomUUID().toString())
            .expirationTime(Utils.calculateTokenExpirationDate())
            .id(3L)
            .user(verified_user)
            .build();
        when(verificationTokenRepository.findByToken(any(String.class)))
            .thenReturn(Optional.ofNullable(expected_object));
        // act
        String actual_response = userService.verifyUser(expected_object.getToken());
        // assert
        assertEquals(expected_response, actual_response);
    }

    @Test
    void testVerifyInvalidTokenUser() {
        String invalidToken = "Invalid token";

        when(verificationTokenRepository.findByToken(any(String.class)))
            .thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> userService.verifyUser(invalidToken))
            .withMessage(invalidToken);
    }

    @Test
    @DisplayName("Test verify user with expired token")
    void testVerifyExpiredTokenUser() {
        // arrange
        String expected_response = "expired token";
        Calendar calender = Calendar.getInstance();
        VerificationToken verificationToken = VerificationToken
            .builder()
            .user(user_one)
            .id(1L)
            .token(UUID.randomUUID().toString())
            .expirationTime(calender.getTime())
            .build();

        when(verificationTokenRepository.findByToken(any(String.class)))
            .thenReturn(Optional.ofNullable(verificationToken));

        // act
        String actual_response = userService.verifyUser(verificationToken.getToken());
        assertEquals(expected_response, actual_response);
    }
}
