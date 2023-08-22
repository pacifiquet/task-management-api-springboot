package com.taskhero.user.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.taskhero.config.db.DatabasePostgresqlTestContainer;
import com.taskhero.config.extension.MockTimeExtension;
import com.taskhero.exceptions.ApiError;
import com.taskhero.user.models.User;
import com.taskhero.user.models.VerificationToken;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.repository.VerificationTokenRepository;
import com.taskhero.utils.Time;
import com.taskhero.utils.Utils;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

  @LocalServerPort private int port;

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  private static WebTestClient client;

  private final VerificationTokenRepository verificationTokenRepository;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserControllerIntegrationTest(
      VerificationTokenRepository verificationTokenRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.verificationTokenRepository = verificationTokenRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

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
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("email", "adminone@gmail.com");
    requestBody.put("first_name", "one");
    requestBody.put("last_name", "username");
    requestBody.put("password", "passworduser");
    requestBody.put("roles", Set.of());
    requestBody.put("permissions", Set.of());

    // act
    var response =
        client
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

    // assert
    assertEquals(HttpStatus.CREATED, response_status);
    assertThat(actual_response).isNotNull();
  }

  @Test
  @DisplayName("Testing Registering user a missing body field")
  void testRegisterUserMissingBodyField() throws Exception {
    // arrange
    var expected_response =
        ApiError.builder()
            .path("/api/v1/users/register")
            .message("user email is required")
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .localDateTime(String.valueOf(Time.currentDateTime()))
            .build();
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("first_name", "usertwo");
    requestBody.put("last_name", "username");
    requestBody.put("password", "jack1223");
    requestBody.put("roles", Set.of());
    requestBody.put("permissions", Set.of());

    // act
    var response =
        client
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

    // assert
    assertEquals(HttpStatus.BAD_REQUEST, response_status);
    assertEquals(expected_response, actual_response);
  }

  @Test
  @DisplayName("Testing Verify Registered User")
  void testVerifyRegisteredUser() throws Exception {
    // arrange
    String expected_response = "User verifies Successfully";
    User user =
        User.builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("admintwo@gmail.com")
            .firstName("userthree")
            .lastName("user")
            .build();
    userRepository.save(user);

    VerificationToken verificationToken =
        verificationTokenRepository.save(
            VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .user(user)
                .build());
    // Build url
    String url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/verify-registration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

    // Act
    var response =
        client.get().uri(new URI(url)).exchange().expectBody(String.class).returnResult();
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
    User user =
        User.builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("adminthree@gmail.com")
            .firstName("userfour")
            .lastName("user")
            .enabled(true)
            .build();
    userRepository.save(user);

    VerificationToken verificationToken =
        verificationTokenRepository.save(
            VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .user(user)
                .build());
    // Build url
    String url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/verify-registration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

    // Act
    var response =
        client.get().uri(new URI(url)).exchange().expectBody(String.class).returnResult();
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
    User user =
        User.builder()
            .password(passwordEncoder.encode("pass1223"))
            .createdAt(Time.currentDateTime())
            .email("adminfive@gmail.com")
            .firstName("userfive")
            .lastName("peter")
            .build();
    userRepository.save(user);

    VerificationToken verificationToken =
        verificationTokenRepository.save(
            VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Calendar.getInstance().getTime())
                .user(user)
                .build());
    // Build url
    String url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/verify-registration")
            .queryParam("token", verificationToken.getToken())
            .toUriString();

    // Act
    var response =
        client.get().uri(new URI(url)).exchange().expectBody(String.class).returnResult();
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
        "{\"path\":\"/api/v1/users/verify-registration\",\"message\":\"Invalid token\",\"statusCode\":500,\"localDateTime\":\"2023-08-08T03:56\"}";
    // Build url
    String url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/verify-registration")
            .queryParam("token", "invalid_token")
            .toUriString();

    // Act
    var response =
        client.get().uri(new URI(url)).exchange().expectBody(String.class).returnResult();
    var actual_response = response.getResponseBody();
    var response_status = response.getStatus();

    // assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response_status);
    assertEquals(expected_response, actual_response);
  }
}
