package com.pacifique.todoapp.config.extension;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TestResult {
    private final String testName;
    private final boolean passed;
}
