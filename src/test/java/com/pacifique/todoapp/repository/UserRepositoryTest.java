package com.pacifique.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.config.db.DatabasePostgresqlTestContainer;
import com.pacifique.todoapp.config.extension.MockTimeExtension;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.model.User;
import java.util.List;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
class UserRepositoryTest {
    public static PostgreSQLContainer<?> postgres = DatabasePostgresqlTestContainer.getInstance();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User userOne;
    private User userTwo;

    @BeforeEach
    void setUp() {
        userOne =
            User
                .builder()
                .firstName("peter")
                .lastName("jack")
                .password(passwordEncoder.encode("password234"))
                .email("peter@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();
        userTwo =
            User
                .builder()
                .firstName("jack")
                .lastName("peter")
                .password(passwordEncoder.encode("password234"))
                .email("jack@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
                .build();
    }

    @Test
    @DisplayName("Registering a user")
    void testRegisterUsers() {
        //Arrange
        var expected_user = userOne;

        // act
        var actual_user = userRepository.save(expected_user);

        //assert
        assertEquals(expected_user, actual_user);
    }

    @Test
    @DisplayName("Testing Get user list")
    void testUserList() {
        //Arrange
        var expected_user_list = List.of(userOne, userTwo);

        //act
        userRepository.saveAll(List.of(userOne, userTwo));
        List<User> actual_user_list = userRepository.findAll();

        //assert
        assertEquals(expected_user_list, actual_user_list);
    }

    @Test
    @DisplayName("Testing Get user")
    void testGetUser() {
        //Arrange
        var expected_user = userOne;
        //act
        userRepository.save(userOne);
        var actual_user = userRepository.findById(1L).get();
        //assert
        assertEquals(expected_user, actual_user);
    }
}
