package com.taskhero.task.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ProjectTaskResponse(
    Long taskId,
    String name,
    String description,
    LocalDate dueDate,
    Boolean priority,
    String status) {}
