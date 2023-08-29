package com.taskhero.user.dto;

import com.taskhero.task.dto.ProjectTaskResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record UserTaskResponse(UserResponse user, List<ProjectTaskResponse> tasks) {}
