package com.taskhero.project.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProjectResponse(
    Long projectId,
    String name,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String description) {}
