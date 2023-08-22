package com.taskhero.project.service;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import com.taskhero.project.models.Project;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public Long createProject(CreateProjectRequest request, Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return projectRepository
        .save(
            Project.builder()
                .user(user)
                .description(request.description())
                .endDate(request.endDate())
                .startDate(request.startDate())
                .name(request.name())
                .build())
        .getProjectId();
  }

  @Override
  public List<ProjectResponse> listProject(Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    List<Project> projects = projectRepository.findByUser(user);
    return projects.stream()
        .map(
            project ->
                ProjectResponse.builder()
                    .projectId(project.getProjectId())
                    .startDate(project.getStartDate())
                    .description(project.getDescription())
                    .endDate(project.getEndDate())
                    .name(project.getName())
                    .build())
        .toList();
  }

  @Override
  public ProjectResponse getProjectById(Long userId, Long projectId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        String.format("user with id: %s not found", userId)));
    List<Project> projects = projectRepository.findByUser(user);
    for (Project project : projects) {
      if (Objects.equals(project.getProjectId(), projectId)) {
        return ProjectResponse.builder()
            .name(project.getName())
            .endDate(project.getEndDate())
            .description(project.getDescription())
            .startDate(project.getStartDate())
            .projectId(project.getProjectId())
            .build();
      }
      throw new IllegalStateException(String.format("project with id: %s not found",projectId));
    }
    return null;
  }

  @Override
  public String inviteUser(Long userId, Long projectId, String email) {
    return "invitation sent";
  }
}
