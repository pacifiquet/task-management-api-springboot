package com.taskhero.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.taskhero.config.db.DatabasePostgresqlTestContainer;
import com.taskhero.config.extension.MockTimeExtension;
import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.PermissionRepository;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.repository.UserRoleRepository;
import com.taskhero.user.security.UserPermission;
import com.taskhero.user.security.UserRole;
import com.taskhero.utils.Time;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockTimeExtension.class)
class AuthServiceImplTest {
  public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

  @BeforeAll
  static void afterAll() {
    postgres.start();
  }

  private final AuthServiceImpl authService;

  private final UserRoleRepository userRoleRepository;
  private final PermissionRepository permissionRepository;
  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  @BeforeEach
  void setUp() {}

  @Autowired
  public AuthServiceImplTest(
      AuthServiceImpl authService,
      UserRoleRepository userRoleRepository,
      PermissionRepository permissionRepository,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.authService = authService;
    this.userRoleRepository = userRoleRepository;
    this.permissionRepository = permissionRepository;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Test
  void authenticate() {
    // arrange
    Permission permission =
        permissionRepository.save(
            Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build());
    Role role = userRoleRepository.save(Role.builder().userRole(UserRole.ROLE_ADMIN).build());
    userRepository.save(
        User.builder()
            .firstName("admin@gmail.com")
            .lastName("admin")
            .email("admin@gmail.com")
            .password(passwordEncoder.encode("password"))
            .roles(Set.of(role))
            .userPermissions(Set.of(permission))
            .enabled(true)
            .createdAt(Time.currentDateTime())
            .build());
    var loginRequest = new LoginRequest("admin@gmail.com", "password");

    // act
    String response = authService.authenticate(loginRequest).getAccessToken();

    // assert
    assertThat(response).isNotNull();
  }
}
