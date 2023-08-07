package com.pacifique.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:12.3-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    private User userOne;
    private User userTwo;

    @BeforeEach
    void setUp() {
        userOne =
            User
                .builder()
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
        userTwo =
            User
                .builder()
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Registering a user")
    void testRegisterUsers() {
        //Arrange
        var user = userOne;

        // act
        var expected_user = userRepository.save(user);

        //assert
        assertEquals(expected_user, user);
    }

    @Test
    @DisplayName("Testing Get user list")
    void testUserList() {
        //Arrange
        var expected_users = List.of(userOne, userTwo);

        //act
        userRepository.saveAll(List.of(userOne, userTwo));
        List<User> userList = userRepository.findAll();

        //assert
        assertEquals(userList, expected_users);
    }

    @Test
    @DisplayName("Testing Get user")
    void testGetUser() {
        //Arrange
        var expected_user = userOne;
        //act
        userRepository.save(userOne);
        var user = userRepository.findById(1L);
        //assert
        assertEquals(user.get(), expected_user);
    }
}
