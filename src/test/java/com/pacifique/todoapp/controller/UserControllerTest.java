package com.pacifique.todoapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Utils;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.exceptions.ApiError;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.model.VerificationToken;
import com.pacifique.todoapp.repository.UserRepository;
import com.pacifique.todoapp.repository.VerificationTokenRepository;
import com.pacifique.todoapp.service.UserServiceImpl;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WebApplicationContext context;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    public UserControllerTest(
        MockMvc mockMvc,
        ObjectMapper objectMapper,
        WebApplicationContext context
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.context = context;
    }

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRequest =
            UserRequest
                .builder()
                .role("user")
                .email("user@gmail.com")
                .firstName("pacifique")
                .lastName("paci")
                .password("password1234")
                .build();
        userResponse =
            UserResponse
                .builder()
                .userId(1L)
                .firstName("peter")
                .lastName("jack")
                .email("user@gmail.com")
                .role("user")
                .createAt(Time.currentDateTime())
                .build();
    }

    @Test
    @DisplayName("Testing register user")
    void testRegisterUser() throws Exception {
        // arrange
        var expected_response = "1";
        when(userService.registerUser(any(UserRequest.class), any())).thenReturn(1L);

        //act
        var actions = mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        );
        var actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isCreated());
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Test get user list")
    void testUserList() throws Exception {
        // arrange
        var expected_response = objectMapper.writeValueAsString(List.of(userResponse));
        when(userService.listOfUser()).thenReturn(List.of(userResponse));

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
        );
        var actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }

    @ParameterizedTest
    @DisplayName("Test get user details")
    @ValueSource(longs = { 1L })
    void testGetUserDetails(long userId) throws Exception {
        // arrange
        var expected_response = objectMapper.writeValueAsString(userResponse);
        when(userService.getUser(anyLong())).thenReturn(userResponse);

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users/{id}", userId).contentType(MediaType.APPLICATION_JSON)
        );
        var actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }

    @ParameterizedTest
    @DisplayName("Test get user details with Invalid Details")
    @ValueSource(longs = { 10L })
    void testGetUserDetailsWithInvalidId(long userId) throws Exception {
        // arrange
        when(userService.getUser(userId))
            .thenThrow(
                new NoSuchElementException("user with id: " + userId + " not found")
            );
        // act
        var actions = mockMvc.perform(
            get("/api/v1/users/{id}", 10L).contentType(MediaType.APPLICATION_JSON)
        );
        // assert
        actions
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("user with id: 10 not found"));
    }

    @Test
    @DisplayName("Test verify user with valid Token")
    void testVerifyUserWithValidToken() throws Exception {
        // arrange
        when(userService.verifyUser(any())).thenReturn("User verifies Successfully");
        String token = UUID.randomUUID().toString();
        String expected_response = "User verifies Successfully";

        var actions = mockMvc.perform(
            get("/api/v1/users/verifyRegistration").param("token", token)
        );
        String actual_response = actions.andReturn().getResponse().getContentAsString();

        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Test verify user with valid Token")
    void testVerifyUserWithIsAlreadyVerified() throws Exception {
        // arrange
        when(userService.verifyUser(any())).thenReturn("user is verified");
        String token = UUID.randomUUID().toString();
        String expected_response = "user is verified";

        var actions = mockMvc.perform(
            get("/api/v1/users/verifyRegistration").param("token", token)
        );
        String actual_response = actions.andReturn().getResponse().getContentAsString();

        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing verify user with invalid token")
    void testVerifyUserWithInvalidToken() throws Exception {
        // arrange
        String token = "Invalid token";
        when(userService.verifyUser(token))
            .thenThrow(new IllegalArgumentException("Invalid token"));

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users/verifyRegistration").param("token", token)
        );

        // assert
        actions
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value(token));
    }

    @Test
    @DisplayName("Testing verify user with expired token")
    void testVerifyUserWithExpiredToken() throws Exception {
        // arrange
        String expected_response = "expired token";
        when(userService.verifyUser("expired_token")).thenReturn(expected_response);

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users/verifyRegistration").param("token", "expired_token")
        );
        String actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }
}
