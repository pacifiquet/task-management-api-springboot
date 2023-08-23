package com.taskhero.project.service;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import java.util.List;

public interface ProjectService {
  Long createProject(CreateProjectRequest createProjectRequest);

  List<ProjectResponse> listProject();

  ProjectResponse getProjectById(Long projectId);

  String addContributor(Long projectId, Long userId);
}
