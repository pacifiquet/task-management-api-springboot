package com.pacifique.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.datetime.extension.MockTimeExtension;
import com.pacifique.todoapp.datetime.extension.TodoAppPostgresqlContainer;
import com.pacifique.todoapp.datetime.utils.Time;
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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ExtendWith(MockTimeExtension.class)
class UserRepositoryTest {
    public static PostgreSQLContainer<?> postgres = TodoAppPostgresqlContainer.getInstance();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
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
                .createdAt(Time.currentDateTime())
                .build();
        userTwo =
            User
                .builder()
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(Time.currentDateTime())
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
        var user = userRepository.findById(1L).get();
        //assert
        assertEquals(user, expected_user);
    }
}
