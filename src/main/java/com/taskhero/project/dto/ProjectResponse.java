package com.taskhero.project.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProjectResponse(
    Long projectId,
    String name,
    String startDate,
    String endDate,
    String description,
    Long owner,
    List<Long> contributors) {}
