package com.pacifique.todoapp;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class MainTest {

    @Test
    void testMain() {
        Main.main(new String[] {});
    }
}
