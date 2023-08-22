package com.taskhero.user.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.taskhero.config.db.DatabasePostgresqlTestContainer;
import com.taskhero.config.extension.MockTimeExtension;
import com.taskhero.exceptions.ApiError;
import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.dto.LoginResponse;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.PermissionRepository;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.repository.UserRoleRepository;
import com.taskhero.user.security.UserPermission;
import com.taskhero.user.security.UserRole;
import com.taskhero.user.service.AuthService;
import com.taskhero.utils.Time;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
public class UserControllerRequiredAuthTest {

  public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

  @LocalServerPort private int port;

  private String baseUrl = "http://localhost:";

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @BeforeAll
  static void setUp() {
    client = WebTestClient.bindToServer().baseUrl("http://localhost:").build();
  }

  @BeforeEach
  public void beforeSetup() {
    this.baseUrl = this.baseUrl + port + "/api/v1/users";
  }

  private static WebTestClient client;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final AuthService authService;
  private User user;
  private Role role;
  private Permission permission;

  @Autowired
  public UserControllerRequiredAuthTest(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      UserRoleRepository roleRepository,
      PermissionRepository permissionRepository,
      AuthService authService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.authService = authService;
  }

  @Test
  @DisplayName("Testing Retrieving movies List")
  void testRetrieveUsersList() throws URISyntaxException {
    // arrange
    permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    role = roleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    user =
        userRepository.save(
            User.builder()
                .email("adminmanagerone@gmail.com")
                .lastName("pac")
                .firstName("pacifique")
                .roles(Set.of(role))
                .userPermissions(Set.of(permission))
                .password(passwordEncoder.encode("pacifique"))
                .createdAt(Time.currentDateTime())
                .enabled(true)
                .build());
    LoginResponse loginResponse =
        authService.authenticate(new LoginRequest("adminmanagerone@gmail.com", "pacifique"));

    var userResponse =
        UserResponse.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .build();

    // act
    var response =
        client
            .get()
            .uri(new URI(baseUrl))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
            .exchange()
            .expectBodyList(UserResponse.class)
            .returnResult();
    var actual_response = response.getResponseBody();
    var response_status = response.getStatus();
    // assert
    assertEquals(HttpStatus.OK, response_status);
    assertThat(actual_response).isNotNull();
  }

  @Test
  @DisplayName("Testing Retrieve User Details")
  void testRetrieveUserDetails() throws URISyntaxException {
    // arrange
    permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    role = roleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    user =
        userRepository.save(
            User.builder()
                .email("adminmanagertwo@gmail.com")
                .lastName("pac")
                .firstName("pacifique")
                .roles(Set.of(role))
                .userPermissions(Set.of(permission))
                .password(passwordEncoder.encode("password"))
                .createdAt(Time.currentDateTime())
                .enabled(true)
                .build());
    var loginResponse =
        authService.authenticate(new LoginRequest("adminmanagertwo@gmail.com", "password"));

    Map<String, Object> urlVariable = new HashMap<>();
    urlVariable.put("id", user.getUserId());
    var url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/{id}")
            .uriVariables(urlVariable)
            .toUriString();

    var expected_response =
        UserResponse.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .build();

    // Act
    var response =
        client
            .get()
            .uri(new URI(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
            .exchange()
            .expectBody(UserResponse.class)
            .returnResult();
    var actual_response = response.getResponseBody();
    var response_status = response.getStatus();

    // assert
    assertEquals(HttpStatus.OK, response_status);
    assertThat(actual_response).isNotNull();
  }

  @Test
  @DisplayName("Testing Retrieve User Details with Invalid")
  void testRetrieveUserDetailsWithInvalidId() throws Exception {
    // arrange
    permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    role = roleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    user =
        userRepository.save(
            User.builder()
                .email("adminmanagerthree@gmail.com")
                .lastName("pac")
                .firstName("pacifique")
                .roles(Set.of(role))
                .userPermissions(Set.of(permission))
                .password(passwordEncoder.encode("password"))
                .createdAt(Time.currentDateTime())
                .enabled(true)
                .build());
    Map<String, Object> urlVariable = new HashMap<>();
    urlVariable.put("id", 100L);
    var url =
        UriComponentsBuilder.fromUriString(this.baseUrl + "/{id}")
            .uriVariables(urlVariable)
            .toUriString();

    var expected_response =
        ApiError.builder()
            .path("/api/v1/users/100")
            .message("user with id: 100 not found")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .localDateTime(String.valueOf(Time.currentDateTime()))
            .build();

    var loginResponse =
        authService.authenticate(new LoginRequest("adminmanagerthree@gmail.com", "password"));
    // Act
    var response =
        client
            .get()
            .uri(new URI(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
            .exchange()
            .expectBody(ApiError.class)
            .returnResult();
    var actual_response = response.getResponseBody();
    var response_status = response.getStatus();

    // assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response_status);
    assertEquals(expected_response, actual_response);
  }
}
