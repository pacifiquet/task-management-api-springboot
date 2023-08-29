package com.taskhero.project.service;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import com.taskhero.project.models.Project;
import com.taskhero.project.models.ProjectContributor;
import com.taskhero.project.repository.ProjectContributorRepository;
import com.taskhero.project.repository.ProjectRepository;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final ProjectContributorRepository contributorRepository;

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
                    .startDate(project.getStartDate().toString())
                    .description(project.getDescription())
                    .endDate(project.getEndDate().toString())
                    .name(project.getName())
                    .owner(project.getOwner().getUserId())
                    .contributors(
                        project.getProjectContributors() == null
                            ? List.of()
                            : project.getProjectContributors().stream()
                                .map(projectContributor -> projectContributor.getUser().getUserId())
                                .toList())
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
                    .endDate(project.getEndDate().toString())
                    .description(project.getDescription())
                    .startDate(project.getStartDate().toString())
                    .projectId(project.getProjectId())
                    .owner(project.getOwner() == null ? null : project.getOwner().getUserId())
                    .contributors(
                        project.getProjectContributors() == null
                            ? List.of()
                            : project.getProjectContributors().stream()
                                .map(projectContributor -> projectContributor.getUser().getUserId())
                                .toList())
                    .build())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("project not found"));
  }

  @Override
  public String addContributor(Long projectId, Long userId) {
    Project project = projectRepository.getReferenceById(projectId);
    User user = userRepository.getReferenceById(userId);
    ProjectContributor projectContributor = new ProjectContributor();
    projectContributor.setProject(project);
    projectContributor.setUser(user);
    contributorRepository.save(projectContributor);
    return "added contributor successfully";
  }

  @Override
  public void deleteProject(Long projectId) {
    Project project = projectRepository.getReferenceById(projectId);
    projectRepository.delete(project);
  }

  @Override
  public Long updateProject(Long projectId, CreateProjectRequest request) {
    Project project = projectRepository.getReferenceById(projectId);
    project.setDescription(request.description());
    project.setName(request.name());
    project.setEndDate(request.endDate());
    project.setStartDate(request.startDate());
    return projectRepository.save(project).getProjectId();
  }
}
