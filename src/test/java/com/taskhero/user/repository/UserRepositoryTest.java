package com.taskhero.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.taskhero.config.db.DatabasePostgresqlTestContainer;
import com.taskhero.config.extension.MockTimeExtension;
import com.taskhero.user.models.User;
import com.taskhero.utils.Time;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
class UserRepositoryTest {
  public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @Mock private PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Autowired
  public UserRepositoryTest(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private User userOne;
  private User userTwo;

  @BeforeEach
  void setUp() {
    userOne =
        User.builder()
            .firstName("peter")
            .lastName("jack")
            .password(passwordEncoder.encode("password234"))
            .email("peter@gmail.com")
            .createdAt(Time.currentDateTime())
            .build();
    userTwo =
        User.builder()
            .firstName("jack")
            .lastName("peter")
            .password(passwordEncoder.encode("password234"))
            .email("jack@gmail.com")
            .createdAt(Time.currentDateTime())
            .build();
  }

  @Test
  @DisplayName("Registering a user")
  void testRegisterUsers() {
    // Arrange
    var expected_user = userOne;
    // act
    var actual_user = userRepository.save(userOne);
    // assert
    assertThat(actual_user).isNotNull();
  }

  @Test
  @DisplayName("Testing Get user list")
  void testUserList() {
    // Arrange
    var expected_user_list = List.of(userOne, userTwo);
    userRepository.saveAllAndFlush(expected_user_list);
    // act
    List<User> actual_user_list = userRepository.findAll();
    // assert
    assertThat(actual_user_list).contains(userOne, userTwo);
  }

  @DisplayName("Testing Get user")
  @ParameterizedTest
  @ValueSource(longs = 1L)
  void testGetUser(long validId) {
    // Arrange
    userRepository.save(userOne);
    // act
    var foundUser = userRepository.findById(validId);
    // assert
    assertThat(foundUser).isNotNull();
  }

  @ParameterizedTest
  @ValueSource(longs = {5L, 6L}) // Add more invalid IDs here
  @DisplayName("Testing Get user with invalid ID")
  void testGetUserWthInvalid(long invalidId) {
    // act
    var foundUser = userRepository.findById(invalidId).orElse(null);
    // assert
    assertThat(foundUser).isNull();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }
}
