package com.taskhero.task.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectTaskResponse(
    Long taskId,
    String name,
    String description,
    LocalDate dueDate,
    Boolean priority,
    String status,
    Long projectId,
    List<Long> users) {}
