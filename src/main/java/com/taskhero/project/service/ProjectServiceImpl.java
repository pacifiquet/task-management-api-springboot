package com.taskhero.project.service;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import com.taskhero.project.models.Project;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  public Long createProject(CreateProjectRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow();
    return projectRepository
        .save(
            Project.builder()
                .owner(user)
                .description(request.description())
                .endDate(request.endDate())
                .startDate(request.startDate())
                .name(request.name())
                .build())
        .getProjectId();
  }

  @Override
  public List<ProjectResponse> listProject() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow();
    List<Project> projects = projectRepository.findByOwner(user);
    return projects.stream()
        .map(
            project ->
                ProjectResponse.builder()
                    .projectId(project.getProjectId())
                    .startDate(project.getStartDate())
                    .description(project.getDescription())
                    .endDate(project.getEndDate())
                    .name(project.getName())
                    .owner(project.getOwner() == null ? null : project.getOwner().getUserId())
                    .contributors(project.getContributors().stream().map(User::getUserId).toList())
                    .build())
        .toList();
  }

  @Override
  public ProjectResponse getProjectById(Long projectId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow();
    return projectRepository.findByOwner(user).stream()
        .filter(project -> project.getProjectId().equals(projectId))
        .map(
            project ->
                ProjectResponse.builder()
                    .name(project.getName())
                    .endDate(project.getEndDate())
                    .description(project.getDescription())
                    .startDate(project.getStartDate())
                    .projectId(project.getProjectId())
                    .owner(project.getOwner() == null ? null : project.getOwner().getUserId())
                    .contributors(project.getContributors().stream().map(User::getUserId).toList())
                    .build())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("project not found"));
  }

  @Override
  public String addContributor(Long projectId, Long userId) {
    Project project =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new IllegalStateException("project not found"));
    Optional<User> user = userRepository.findById(userId);

    if (user.isPresent()) {
      project.getContributors().add(user.get());
      projectRepository.save(project);
      return "added contributor successfully";
    }
    return "failed to add a contributor";
  }
}
