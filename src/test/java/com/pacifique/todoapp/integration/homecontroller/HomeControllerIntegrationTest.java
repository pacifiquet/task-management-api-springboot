package com.pacifique.todoapp.integration.homecontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pacifique.todoapp.config.db.TodoAppPostgresqlContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HomeControllerIntegrationTest {
    public static PostgreSQLContainer<?> postgres = TodoAppPostgresqlContainer.getInstance();

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost:";

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    private static WebTestClient client;

    @BeforeAll
    static void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:").build();
    }

    @BeforeEach
    public void beforeSetup() {
        this.baseUrl = this.baseUrl + port + "/";
    }

    @Test
    @DisplayName("Testing Welcome message")
    void testWelcomeMessage() throws Exception {
        //Arrange
        var expected_response = "{\"message\":\"Welcome to app todo.\"}";

        // Act
        var response = client
            .get()
            .uri(baseUrl)
            .exchange()
            .expectBody(String.class)
            .returnResult();
        String actual_response = response.getResponseBody();
        HttpStatusCode response_status = response.getStatus();

        // assert
        assertEquals(HttpStatus.OK, response_status);
        assertEquals(expected_response, actual_response);
    }
}
