package com.pacifique.todoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MainTest {

    @Test
    void testMain() {
        Main.main(new String[] {});
    }
}
