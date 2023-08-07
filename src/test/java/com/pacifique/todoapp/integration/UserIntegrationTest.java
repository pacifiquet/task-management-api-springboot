package com.pacifique.todoapp.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.repository.UserRepository;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = { "server.port=8042" }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserIntegrationTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:12.3-alpine"
    )
    .withDatabaseName("testdb");

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

    private static WebTestClient client;
    private final UserRepository userRepository;

    //    @LocalServerPort
    //    private int port;

    private String baseUrl = "http://localhost:";

    @Autowired
    public UserIntegrationTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeAll
    static void setUp() {
        client =
            WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + "8042")
                .build();
    }

    @BeforeEach
    public void beforeSetup() {
        this.baseUrl = this.baseUrl + "8042" + "/api/v1/users";
    }

    @Test
    @DisplayName("Testing Registering user")
    void testRegisterUser() throws Exception {
        // arrange
        var expected_response_data = "1";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "user@gmail.com");
        requestBody.put("fullName", "username");
        requestBody.put("role", "user");

        // act
        var response = client
            .post()
            .uri(new URI(baseUrl + "/register"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .exchange()
            .expectBody(String.class)
            .returnResult();
        var response_data = response.getResponseBody();
        var response_status = response.getStatus();

        //assert
        assertEquals(response_status, HttpStatus.CREATED);
        assertEquals(response_data, expected_response_data);
    }

    @Test
    @DisplayName("Testing Retrieving movies List")
    void testRetrieveUsersList() throws Exception {
        // arrange
        var userResponse = UserResponse
            .builder()
            .id(2L)
            .role("user")
            .createAt(LocalDateTime.parse("2023-08-07T16:13:46.853683"))
            .fullName("user")
            .email("user@gmail.com")
            .build();
        var expected_response = List.of(userResponse);
        userRepository.save(
            User
                .builder()
                .email("user@gmail.com")
                .fullName("user")
                .createdAt(LocalDateTime.parse("2023-08-07T16:13:46.853683"))
                .todos(null)
                .role("user")
                .build()
        );

        // act
        var response = client
            .get()
            .uri(new URI(baseUrl))
            .exchange()
            .expectBodyList(UserResponse.class)
            .returnResult();
        var response_data = response.getResponseBody();
        var status = response.getStatus();

        // assert
        assertEquals(status, HttpStatus.OK);
        assertEquals(response_data, expected_response);
    }
}
