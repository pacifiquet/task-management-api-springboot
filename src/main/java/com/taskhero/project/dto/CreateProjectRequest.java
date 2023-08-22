package com.taskhero.project.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateProjectRequest(
    @NotNull String name,
    @NotNull String description,
    @NotNull LocalDateTime startDate,
    @NotNull LocalDateTime endDate) {}
