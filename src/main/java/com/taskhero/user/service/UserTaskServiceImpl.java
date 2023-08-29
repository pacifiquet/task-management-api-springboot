package com.taskhero.user.service;

import com.taskhero.task.dto.ProjectTaskResponse;
import com.taskhero.task.models.ProjectTask;
import com.taskhero.task.repository.ProjectTaskRepository;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.dto.UserTaskResponse;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {
  private final UserRepository userRepository;
  private final ProjectTaskRepository projectTaskRepository;

  @Override
  public UserTaskResponse userTasks() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow();
    Set<ProjectTask> userTasks = projectTaskRepository.findByAssignedTo(user);
    return UserTaskResponse.builder()
        .user(getUserResponse().apply(user, new UserResponse()))
        .tasks(getTaskResponses().apply(userTasks))
        .build();
  }

  @Override
  public BiFunction<User, UserResponse, UserResponse> getUserResponse() {
    return (user, userResponse) ->
        UserResponse.builder()
            .email(user.getEmail())
            .lastName(user.getLastName())
            .firstName(user.getFirstName())
            .userId(user.getUserId())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .roles(user.getRoles().stream().map(role -> role.getUserRole().name()).toList())
            .permissions(
                user.getUserPermissions().stream()
                    .map(permission -> permission.getUserPermissions().name())
                    .toList())
            .build();
  }

  @Override
  public Function<Set<ProjectTask>, List<ProjectTaskResponse>> getTaskResponses() {
    return (projectTasks ->
        projectTasks.stream()
            .map(
                projectTask ->
                    ProjectTaskResponse.builder()
                        .taskId(projectTask.getTaskId())
                        .name(projectTask.getName())
                        .projectId(projectTask.getProject().getProjectId())
                        .assignedTo(projectTask.getTaskId())
                        .status(projectTask.getStatus())
                        .description(projectTask.getDescription())
                        .dueDate(projectTask.getDueDate().toString())
                        .priority(projectTask.getPriority())
                        .build())
            .toList());
  }
}
