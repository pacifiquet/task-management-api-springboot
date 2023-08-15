package com.pacifique.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.pacifique.todoapp.config.db.DatabasePostgresqlTestContainer;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.Utils;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.model.VerificationToken;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockTimeExtension.class)
public class VerificationTokenRepositoryTest {
    public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private VerificationToken verificationToken_one;
    private VerificationToken verificationToken_two;
    private User user_one;
    private User user_two;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void setUp() {
        user_one =
            User
                .builder()
                .firstName("peter")
                .lastName("jack")
                .password(passwordEncoder.encode("password234"))
                .email("peter@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();
        user_two =
            User
                .builder()
                .firstName("jack")
                .lastName("peter")
                .password(passwordEncoder.encode("password234"))
                .email("jack@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();

        verificationToken_one =
            VerificationToken
                .builder()
                .user(user_one)
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .build();

        verificationToken_two =
            VerificationToken
                .builder()
                .user(user_two)
                .token(UUID.randomUUID().toString())
                .expirationTime(Utils.calculateTokenExpirationDate())
                .build();
    }

    @Test
    @DisplayName("Create verification Token")
    void testCreateVerificationToken() {
        // arrange
        var expected_verification_token = verificationToken_one;
        // act
        userRepository.save(user_one);
        var actual_verification_token = tokenRepository.save(verificationToken_one);
        // assert
        assertEquals(expected_verification_token, actual_verification_token);
    }

    @Test
    @DisplayName("Find Verification Token")
    void testFindByToken() {
        // arrange
        var expected_verification_token = verificationToken_two;
        // act
        userRepository.save(user_two);
        tokenRepository.save(verificationToken_two);
        var actual_verification_token = tokenRepository
            .findByToken(verificationToken_two.getToken())
            .get();
        // assert
        assertEquals(expected_verification_token, actual_verification_token);
    }

    @Test
    @DisplayName("Find Verification Token with Invalid Token")
    void testFindByTokenPassedInvalidToken() {
        // arrange
        var expected_verification_token = Optional.empty();
        // act
        userRepository.save(user_two);
        tokenRepository.save(verificationToken_two);
        var actual_verification_token = tokenRepository.findByToken("invalid token");
        // assert
        assertEquals(expected_verification_token, actual_verification_token);
    }
}
