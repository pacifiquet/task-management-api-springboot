package com.taskhero.task.service;

import com.taskhero.project.models.Project;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.task.dto.CreateTaskRequest;
import com.taskhero.task.dto.ProjectTaskResponse;
import com.taskhero.task.models.ProjectTask;
import com.taskhero.task.repository.ProjectTaskRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {
  private final ProjectTaskRepository projectTaskRepository;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  @PersistenceContext private EntityManager entityManager;

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
            projectTask ->
                ProjectTaskResponse.builder()
                    .taskId(projectTask.getTaskId())
                    .description(projectTask.getDescription())
                    .dueDate(projectTask.getDueDate().toString())
                    .name(projectTask.getName())
                    .priority(projectTask.getPriority())
                    .status(projectTask.getStatus())
                    .assignedTo(
                        projectTask.getAssignedTo() == null
                            ? null
                            : projectTask.getAssignedTo().getUserId())
                    .projectId(projectTask.getProject().getProjectId())
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
                    .dueDate(projectTask.getDueDate().toString())
                    .description(projectTask.getDescription())
                    .priority(projectTask.getPriority())
                    .taskId(projectTask.getTaskId())
                    .status(projectTask.getStatus())
                    .assignedTo(
                        projectTask.getAssignedTo() == null
                            ? null
                            : projectTask.getAssignedTo().getUserId())
                    .projectId(projectTask.getProject().getProjectId())
                    .build())
        .findFirst()
        .orElseThrow(
            () -> new IllegalStateException(String.format("task id: %s not found", taskId)));
  }

  @Override
  @Transactional
  public String assignUserToTask(Long projectId, Long userId, Long taskId) {
    Optional<Project> project = projectRepository.findById(projectId);
    Optional<User> user = userRepository.findById(userId);
    Optional<ProjectTask> task = projectTaskRepository.findById(taskId);

    if (project.isEmpty()) {
      throw new IllegalStateException("project not found");
    } else if (user.isEmpty()) {
      throw new IllegalStateException("user not found");
    } else if (task.isEmpty()) {
      throw new IllegalStateException(String.format("task id: %s not found", taskId));
    } else if (!project.get().getProjectContributors().stream()
        .map(projectContributor -> projectContributor.getUser().getUserId())
        .toList()
        .contains(userId)) {
      throw new IllegalStateException("user not found in the project");
    } else if (task.get().getAssignedTo() == user.get()) {
      return "user already assigned";

    } else {
      task.get().setAssignedTo(user.get());
      projectTaskRepository.save(task.get());
      return "successfully assigned";
    }
  }

  @Override
  public void deleteTask(Long projectId, Long taskId) {
    Project project = projectRepository.getReferenceById(projectId);
    List<ProjectTask> tasks = projectTaskRepository.findByProject(project);
    ProjectTask task =
        tasks.stream()
            .filter(projectTask -> projectTask.getTaskId().equals(taskId))
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("deleting task failed task with id: %s not found", taskId)));
    projectTaskRepository.delete(task);
  }

  @Override
  public Long updateTask(Long projectId, Long taskId, CreateTaskRequest request) {
    Project project = projectRepository.getReferenceById(projectId);
    List<ProjectTask> tasks = projectTaskRepository.findByProject(project);
    ProjectTask task =
        tasks.stream()
            .filter(projectTask -> projectTask.getTaskId().equals(taskId))
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("failed to update task with id: %s not found", taskId)));
    task.setDescription(request.description());
    task.setName(request.name());
    task.setPriority(request.priority());
    task.setStatus(request.status());
    task.setDueDate(request.dueDate());

    return projectTaskRepository.save(task).getTaskId();
  }
}
