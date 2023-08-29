package com.taskhero.user.service;

import com.taskhero.task.dto.ProjectTaskResponse;
import com.taskhero.task.models.ProjectTask;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.dto.UserTaskResponse;
import com.taskhero.user.models.User;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface UserTaskService {
  UserTaskResponse userTasks();

  BiFunction<User, UserResponse, UserResponse> getUserResponse();

  Function<Set<ProjectTask>, List<ProjectTaskResponse>> getTaskResponses();
}
