package com.taskhero.task.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateTaskRequest(
    @NotNull String name,
    @NotNull String description,
    @NotNull LocalDate dueDate,
    @NotNull Boolean priority,
    @NotNull String status) {}
