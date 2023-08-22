package com.taskhero.project.service;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import java.util.List;

public interface ProjectService {
  Long createProject(CreateProjectRequest createProjectRequest, Long userId);

  List<ProjectResponse> listProject(Long userId);

  ProjectResponse getProjectById(Long userId, Long projectId);

  String inviteUser(Long userId, Long projectId, String email);
}
