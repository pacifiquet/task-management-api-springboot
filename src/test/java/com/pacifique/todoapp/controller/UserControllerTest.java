package com.pacifique.todoapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Time;
import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
class UserControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest =
            UserRequest
                .builder()
                .role("user")
                .email("user@gmail.com")
                .fullName("username")
                .build();
        userResponse =
            UserResponse
                .builder()
                .id(1L)
                .fullName("username")
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
        when(userService.registerUser(any(UserRequest.class))).thenReturn(1L);

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
        when(userService.allUsers()).thenReturn(List.of(userResponse));

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
        );
        var actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Test get user details")
    void testGetUserDetails() throws Exception {
        // arrange
        var expected_response = objectMapper.writeValueAsString(userResponse);
        when(userService.getUser(anyLong())).thenReturn(userResponse);

        // act
        var actions = mockMvc.perform(
            get("/api/v1/users/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
        );
        var actual_response = actions.andReturn().getResponse().getContentAsString();

        // assert
        actions.andExpect(status().isOk());
        assertEquals(expected_response, actual_response);
    }
}
