package com.pacifique.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.pacifique.todoapp.model.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private final UserRepository userRepository;
    private User userOne;
    private User userTwo;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        this.userOne =
            User
                .builder()
                .id(1L)
                .fullName("emanuel k")
                .email("emaul@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
        this.userTwo =
            User
                .builder()
                .id(2L)
                .fullName("peter p")
                .email("peter@gmail.com")
                .role("user")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Registering a user")
    void testRegisterUser() {
        // act
        User user = userRepository.save(this.userOne);
        //assert
        assertNotNull(user);
        assertEquals(this.userOne, user);
    }

    @AfterEach
    void tearDown() {}
}
