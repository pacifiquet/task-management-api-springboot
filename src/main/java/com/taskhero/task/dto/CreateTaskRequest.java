package com.taskhero.task.dto;

import java.time.LocalDate;

public record CreateTaskRequest(
    String name, String description, LocalDate dueDate, Boolean priority, String status) {}
