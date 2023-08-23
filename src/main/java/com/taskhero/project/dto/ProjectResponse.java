package com.taskhero.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectResponse(
    Long projectId,
    String name,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String description,
    Long owner,
    List<Long> contributors) {}
