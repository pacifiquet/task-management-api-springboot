package com.taskhero.task.dto;

import lombok.Builder;

@Builder
public record ProjectTaskResponse(
    Long taskId,
    String name,
    String description,
    String dueDate,
    Boolean priority,
    String status,
    Long projectId,
    Long assignedTo) {}
