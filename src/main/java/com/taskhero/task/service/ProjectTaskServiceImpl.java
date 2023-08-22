package com.taskhero.task.service;

import com.taskhero.project.models.Project;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.task.dto.CreateTaskRequest;
import com.taskhero.task.dto.ProjectTaskResponse;
import com.taskhero.task.models.ProjectTask;
import com.taskhero.task.repository.ProjectTaskRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {
  private final ProjectTaskRepository projectTaskRepository;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public Long addTaskProject(CreateTaskRequest request, Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow();
    return projectTaskRepository
        .save(
            ProjectTask.builder()
                .name(request.name())
                .dueDate(request.dueDate())
                .status(request.status())
                .priority(request.priority())
                .description(request.description())
                .project(project)
                .build())
        .getTaskId();
  }

  @Override
  @Transactional
  public String assignUserToTask(Long userId, Long taskId) {
    ProjectTask projectTask = projectTaskRepository.findById(taskId).orElseThrow();
    User user = userRepository.findById(userId).orElseThrow();
    projectTask.setUser(user);
    projectTaskRepository.save(projectTask);
    return "successfully assigned";
  }

  @Override
  public List<ProjectTaskResponse> taskList(Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow();
    List<ProjectTask> byProject = projectTaskRepository.findByProject(project);
    return byProject.stream()
        .map(
            t ->
                ProjectTaskResponse.builder()
                    .taskId(t.getTaskId())
                    .description(t.getDescription())
                    .dueDate(t.getDueDate())
                    .name(t.getName())
                    .priority(t.getPriority())
                    .status(t.getStatus())
                    .build())
        .toList();
  }

  @Override
  public ProjectTaskResponse getTaskProjectById(Long taskId, Long projectId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("project with id :%s not found", projectId)));
    List<ProjectTask> tasks = projectTaskRepository.findByProject(project);
    for (ProjectTask projectTask : tasks) {
      if (Objects.equals(projectTask.getTaskId(), taskId)) {
        return ProjectTaskResponse.builder()
            .taskId(projectTask.getTaskId())
            .description(projectTask.getDescription())
            .dueDate(projectTask.getDueDate())
            .name(projectTask.getName())
            .priority(projectTask.getPriority())
            .status(projectTask.getStatus())
            .build();
      }
      throw new IllegalStateException(String.format("task with id: %s not found", taskId));
    }
    return null;
  }
}
