package com.taskhero.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskhero.config.authentication.AuthenticationHandler;
import com.taskhero.config.db.DatabasePostgresqlTestContainer;
import com.taskhero.config.extension.MockTimeExtension;
import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.dto.UserRegisterRequest;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.models.VerificationToken;
import com.taskhero.user.repository.PermissionRepository;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.repository.UserRoleRepository;
import com.taskhero.user.repository.VerificationTokenRepository;
import com.taskhero.user.security.UserPermission;
import com.taskhero.user.security.UserRole;
import com.taskhero.user.service.AuthService;
import com.taskhero.utils.Time;
import com.taskhero.utils.Utils;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockTimeExtension.class)
class UserControllerTest {
  private static final PostgreSQLContainer<?> postgres =
      DatabasePostgresqlTestContainer.getInstance();
  private static final AuthenticationHandler authenticationHandler =
      AuthenticationHandler.getInstance();

  @BeforeAll
  static void afterAll() {
    authenticationHandler.start();
    postgres.start();
  }

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final WebApplicationContext context;

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final PermissionRepository permissionRepository;
  private final VerificationTokenRepository verificationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthService authService;
  private Permission permission;
  private Role role;
  private UserResponse userResponse;
  private User user;

  @Autowired
  public UserControllerTest(
      ObjectMapper objectMapper,
      WebApplicationContext context,
      UserRepository userRepository,
      UserRoleRepository userRoleRepository,
      PermissionRepository permissionRepository,
      VerificationTokenRepository verificationTokenRepository,
      PasswordEncoder passwordEncoder,
      AuthService authService) {
    this.objectMapper = objectMapper;
    this.context = context;
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
    this.permissionRepository = permissionRepository;
    this.verificationTokenRepository = verificationTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.authService = authService;
  }

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  @DisplayName("Testing register user")
  void testRegisterUser() throws Exception {
    // arrange
    var userRequest =
        UserRegisterRequest.builder()
            .email("user@gmail.com")
            .firstName("pacifique")
            .lastName("paci")
            .password("password1234")
            .permissions(Set.of())
            .roles(Set.of())
            .build();
    // act
    var actions =
        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
    var response = actions.andReturn().getResponse().getContentAsString();

    // assert
    actions.andExpect(status().isCreated());
    assertThat(response).isNotNull();
  }

  @Test
  @DisplayName("Test get user list")
  void testUserList() throws Exception {
    // arrange
    permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    role = userRoleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    user =
        userRepository.save(
            User.builder()
                .email("peter@gmail.com")
                .lastName("pac")
                .firstName("pacifique")
                .roles(Set.of(role))
                .userPermissions(Set.of(permission))
                .password(passwordEncoder.encode("password"))
                .createdAt(Time.currentDateTime())
                .enabled(true)
                .build());
    userResponse =
        UserResponse.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .build();
    var expected_response = objectMapper.writeValueAsString(userResponse);
    var loginResponse = authService.authenticate(new LoginRequest("peter@gmail.com", "password"));

    // act
    var actions =
        mockMvc.perform(
            get("/api/v1/users")
                .header("Authorization", "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON));
    var actual_response = actions.andReturn().getResponse().getContentAsString();

    // assert
    actions.andExpect(status().isOk());
    assertThat(actual_response).isNotNull();
  }

  @Test
  @DisplayName("Test get user details")
  void testGetUserDetails() throws Exception {
    // arrange
    permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    role = userRoleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    user =
        userRepository.save(
            User.builder()
                .email("userone@gmail.com")
                .lastName("userone")
                .firstName("user")
                .roles(Set.of(role))
                .userPermissions(Set.of(permission))
                .password(passwordEncoder.encode("password"))
                .createdAt(Time.currentDateTime())
                .enabled(true)
                .build());
    userResponse =
        UserResponse.builder()
            .userId(user.getUserId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .build();
    var expected_response = objectMapper.writeValueAsString(userResponse);
    var loginResponse = authService.authenticate(new LoginRequest("userone@gmail.com", "password"));

    // act
    var actions =
        mockMvc.perform(
            get("/api/v1/users/{id}", user.getUserId())
                .header("Authorization", "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON));
    var actual_response = actions.andReturn().getResponse().getContentAsString();

    // assert
    actions.andExpect(status().isOk());
    assertThat(actual_response).isNotNull();
  }

  @ParameterizedTest
  @DisplayName("Test get user details with Invalid Details")
  @ValueSource(longs = {10L})
  void testGetUserDetailsWithInvalidId(long userId) throws Exception {
    // arrange
    userRepository.save(
        User.builder()
            .email("usertwo@gmail.com")
            .lastName("usertwo")
            .firstName("user")
            .roles(Set.of(userRoleRepository.findByUserRole(UserRole.ROLE_ADMIN).orElseThrow()))
            .userPermissions(
                Set.of(
                    permissionRepository
                        .findByUserPermissions(UserPermission.PROJECT_WRITE)
                        .orElseThrow()))
            .password(passwordEncoder.encode("password"))
            .createdAt(Time.currentDateTime())
            .enabled(true)
            .build());
    var loginResponse = authService.authenticate(new LoginRequest("usertwo@gmail.com", "password"));

    // act
    var actions =
        mockMvc.perform(
            get("/api/v1/users/{id}", userId)
                .header("Authorization", "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON));
    // assert
    actions
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("user with id: 10 not found"));
  }

  @Test
  @DisplayName("Test verify user with valid AuthJwtToken")
  void testVerifyUserWithValidToken() throws Exception {
    // arrange
    String expected_response = "User verifies Successfully";
    String token = UUID.randomUUID().toString();
    user =
        userRepository.save(
            User.builder()
                .password(passwordEncoder.encode("password"))
                .roles(Set.of())
                .userPermissions(Set.of())
                .email("userthree@gmail.com")
                .createdAt(Time.currentDateTime())
                .firstName("userthree")
                .lastName("last")
                .build());
    verificationTokenRepository.save(
        VerificationToken.builder()
            .user(user)
            .token(token)
            .expirationTime(Utils.calculateTokenExpirationDate())
            .build());
    // act
    var actions = mockMvc.perform(get("/api/v1/users/verify-registration").param("token", token));
    String actual_response = actions.andReturn().getResponse().getContentAsString();

    // assert
    actions.andExpect(status().isOk());
    assertEquals(expected_response, actual_response);
  }

  @Test
  @DisplayName("Test verify user with valid AuthJwtToken")
  void testVerifyUserWithIsAlreadyVerified() throws Exception {
    // arrange
    String token = UUID.randomUUID().toString();
    String expected_response = "user is verified";
    user =
        userRepository.save(
            User.builder()
                .password(passwordEncoder.encode("password"))
                .roles(Set.of())
                .userPermissions(Set.of())
                .email("userfour@gmail.com")
                .createdAt(Time.currentDateTime())
                .firstName("userfour")
                .lastName("user")
                .build());
    user.setEnabled(true);
    User verifiedUser = userRepository.save(user);
    verificationTokenRepository.save(
        VerificationToken.builder()
            .user(verifiedUser)
            .token(token)
            .expirationTime(Utils.calculateTokenExpirationDate())
            .build());

    var actions = mockMvc.perform(get("/api/v1/users/verify-registration").param("token", token));
    String actual_response = actions.andReturn().getResponse().getContentAsString();

    actions.andExpect(status().isOk());
    assertEquals(expected_response, actual_response);
  }

  @Test
  @DisplayName("Testing verify user with invalid token")
  void testVerifyUserWithInvalidToken() throws Exception {
    // arrange
    String token = "Invalid token";
    // act
    var actions = mockMvc.perform(get("/api/v1/users/verify-registration").param("token", token));

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
    String token = UUID.randomUUID().toString();
    user =
        userRepository.save(
            User.builder()
                .password(passwordEncoder.encode("pass"))
                .roles(Set.of())
                .userPermissions(Set.of())
                .email("userfive@gmail.com")
                .createdAt(Time.currentDateTime())
                .firstName("first")
                .lastName("last")
                .build());
    verificationTokenRepository.save(
        VerificationToken.builder()
            .user(user)
            .token(token)
            .expirationTime(new Date(Time.currentTimeMillis()))
            .build());

    // act
    var actions = mockMvc.perform(get("/api/v1/users/verify-registration").param("token", token));
    String actual_response = actions.andReturn().getResponse().getContentAsString();

    // assert
    actions.andExpect(status().isOk());
    assertEquals(expected_response, actual_response);
  }
}
