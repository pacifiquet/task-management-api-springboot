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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {
  private final ProjectTaskRepository projectTaskRepository;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public Long addTaskProject(Long projectId, CreateTaskRequest request) {
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
  public String assignUserToTask(Long projectId, Long userId, Long taskId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("project with id: %s not found", projectId)));
    User user = userRepository.findById(userId).orElseThrow();

    if (!project.getContributors().contains(user)) {
      throw new IllegalStateException("user do not belong in this project");
    }

    List<ProjectTask> taskList = projectTaskRepository.findByProject(project);
    ProjectTask projectTask =
        taskList.stream()
            .filter(t -> t.getTaskId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("task not found"));

    projectTask.getUsers().add(user);
    projectTaskRepository.save(projectTask);
    return "successfully assigned";
  }

  @Override
  public List<ProjectTaskResponse> taskList(Long projectId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("project id: %s not found", projectId)));

    List<ProjectTask> tasks = projectTaskRepository.findByProject(project);
    return tasks.stream()
        .map(
            t ->
                ProjectTaskResponse.builder()
                    .taskId(t.getTaskId())
                    .description(t.getDescription())
                    .dueDate(t.getDueDate())
                    .name(t.getName())
                    .priority(t.getPriority())
                    .status(t.getStatus())
                    .projectId(t.getProject().getProjectId())
                    .users(t.getUsers().stream().map(User::getUserId).toList())
                    .build())
        .toList();
  }

  @Override
  public ProjectTaskResponse getTaskProjectById(Long projectId, Long taskId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("project with id :%s not found", projectId)));

    List<ProjectTask> tasks = projectTaskRepository.findByProject(project);
    return tasks.stream()
        .filter(task -> task.getTaskId().equals(taskId))
        .map(
            projectTask ->
                ProjectTaskResponse.builder()
                    .name(projectTask.getName())
                    .dueDate(projectTask.getDueDate())
                    .description(projectTask.getDescription())
                    .priority(projectTask.getPriority())
                    .taskId(projectTask.getTaskId())
                    .status(projectTask.getStatus())
                    .projectId(projectTask.getProject().getProjectId())
                    .users(projectTask.getUsers().stream().map(User::getUserId).toList())
                    .build())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("task not found"));
  }
}
