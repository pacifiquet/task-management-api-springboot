package com.pacifique.todoapp.integration.usercontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.config.db.DatabasePostgresqlTestContainer;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Utils;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.exceptions.ApiError;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.model.VerificationToken;
import com.pacifique.todoapp.repository.UserRepository;
import com.pacifique.todoapp.repository.VerificationTokenRepository;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
public class UserControllerIntegrationTest {
    public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    private static WebTestClient client;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl = "http://localhost:";

    @BeforeAll
    static void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:").build();
    }

    @BeforeEach
    public void beforeSetup() {
        this.baseUrl = this.baseUrl + port + "/api/v1/users";
    }

    @Test
    @DisplayName("Testing Registering user")
    void testRegisterUser() throws Exception {
        // arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "jack@gmail.com");
        requestBody.put("first_name", "jack");
        requestBody.put("last_name", "peter");
        requestBody.put("password", "jack1223");
        requestBody.put("role", "user");

        // act
        var response = client
            .post()
            .uri(new URI(baseUrl + "/register"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        //assert
        assertEquals(HttpStatus.CREATED, response_status);
        assertThat(actual_response).isNotNull();
    }

    @Test
    @DisplayName("Testing Registering user a missing body field")
    void testRegisterUserMissingBodyField() throws Exception {
        // arrange
        var expected_response = ApiError
            .builder()
            .path("/api/v1/users/register")
            .message("user email is required")
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .localDateTime(Time.currentDateTime())
            .build();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("first_name", "jack");
        requestBody.put("last_name", "peter");
        requestBody.put("password", "jack1223");
        requestBody.put("role", "user");

        // act
        var response = client
            .post()
            .uri(new URI(baseUrl + "/register"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .exchange()
            .expectBody(ApiError.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        //assert
        assertEquals(HttpStatus.BAD_REQUEST, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Retrieving movies List")
    void testRetrieveUsersList() throws Exception {
        // arrange
        var userResponse = UserResponse
            .builder()
            .userId(1L)
            .email("jack@gmail.com")
            .firstName("jack")
            .lastName("peter")
            .enabled(false)
            .role("user")
            .createAt(Time.currentDateTime())
            .build();

        // act
        var response = client
            .get()
            .uri(new URI(baseUrl))
            .exchange()
            .expectBodyList(UserResponse.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();
        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertThat(actual_response).contains(userResponse);
    }

    @Test
    @DisplayName("Testing Retrieve User Details")
    void testRetrieveUserDetails() throws Exception {
        // arrange
        Map<String, Object> urlVariable = new HashMap<>();
        urlVariable.put("id", 1L);
        var url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/{id}")
            .uriVariables(urlVariable)
            .toUriString();
        var expected_response = UserResponse
            .builder()
            .userId(1L)
            .email("jack@gmail.com")
            .firstName("jack")
            .lastName("peter")
            .enabled(false)
            .role("user")
            .createAt(Time.currentDateTime())
            .build();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(UserResponse.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Retrieve User Details with Invalid")
    void testRetrieveUserDetailsWithInvalidId() throws Exception {
        // arrange
        Map<String, Object> urlVariable = new HashMap<>();
        urlVariable.put("id", 100L);
        var url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/{id}")
            .uriVariables(urlVariable)
            .toUriString();

        var expected_response = ApiError
            .builder()
            .path("/api/v1/users/100")
            .message("user with id: 100 not found")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .localDateTime(Time.currentDateTime())
            .build();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(ApiError.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Verify Registered User")
    void testVerifyRegisteredUser() throws Exception {
        // arrange
        String expected_response = "User verifies Successfully";
        User user = User
            .builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("jack@gmail.com")
            .firstName("jack")
            .lastName("peter")
            .role("user")
            .build();
        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenRepository.save(
            VerificationToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .user(user)
                .build()
        );
        // Build url
        String url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/verifyRegistration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Verifying Already verified User")
    void testVerifyAlreadyVerifiedUser() throws Exception {
        // arrange
        String expected_response = "user is verified";
        User user = User
            .builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("jack@gmail.com")
            .firstName("jack")
            .lastName("peter")
            .role("user")
            .enabled(true)
            .build();
        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenRepository.save(
            VerificationToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .user(user)
                .build()
        );
        // Build url
        String url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/verifyRegistration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Verifying User with expired token")
    void testVerifyUserWithExpiredToken() throws Exception {
        // arrange
        String expected_response = "expired token";
        User user = User
            .builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("jack@gmail.com")
            .firstName("jack")
            .lastName("peter")
            .role("user")
            .build();
        userRepository.save(user);

        VerificationToken verificationToken = verificationTokenRepository.save(
            VerificationToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Calendar.getInstance().getTime())
                .user(user)
                .build()
        );
        // Build url
        String url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/verifyRegistration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertEquals(expected_response, actual_response);
    }

    @Test
    @DisplayName("Testing Verifying User with invalid token")
    void testVerifyUserWithInvalidToken() throws Exception {
        // arrange
        var expected_response =
            "{\"path\":\"/api/v1/users/verifyRegistration\",\"message\":\"Invalid token\",\"statusCode\":500,\"localDateTime\":\"2023-08-08T03:56:00\"}";
        // Build url
        String url = UriComponentsBuilder
            .fromUriString(this.baseUrl + "/verifyRegistration")
            .queryParam("token", "invalid_token")
            .toUriString();

        // Act
        var response = client
            .get()
            .uri(new URI(url))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var actual_response = response.getResponseBody();
        var response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response_status);
        assertEquals(expected_response, actual_response);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
