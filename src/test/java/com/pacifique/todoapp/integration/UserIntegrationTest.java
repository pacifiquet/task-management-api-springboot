package com.pacifique.todoapp.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.datetime.extension.MockTimeExtension;
import com.pacifique.todoapp.datetime.extension.TodoAppPostgresqlContainer;
import com.pacifique.todoapp.datetime.utils.Time;
import com.pacifique.todoapp.dto.UserResponse;
import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = { "server.port=8042" }
)
@ActiveProfiles("test")
@ExtendWith({ MockTimeExtension.class, MockitoExtension.class })
public class UserIntegrationTest {
    public static PostgreSQLContainer<?> postgres = TodoAppPostgresqlContainer.getInstance();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    private static WebTestClient client;

    private String baseUrl = "http://localhost:";

    @BeforeAll
    static void setUp() {
        client =
            WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + "8042")
                .build();
        Clock clock = Clock.fixed(
            Instant.parse("2014-12-22T10:15:30.00Z"),
            ZoneId.of("UTC")
        );

        LocalDateTime dateTime = LocalDateTime.now(clock);
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
            .id(1L)
            .email("user@gmail.com")
            .fullName("username")
            .role("role")
            .createAt(Time.currentDateTime())
            .build();
        var expected_response = List.of(userResponse);

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
        //        assertEquals(response_data, expected_response);
    }
}
