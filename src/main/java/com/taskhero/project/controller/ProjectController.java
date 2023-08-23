package com.taskhero.project.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.taskhero.project.dto.CreateProjectRequest;
import com.taskhero.project.dto.ProjectResponse;
import com.taskhero.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Project Controller")
@RequestMapping("/api/v1/projects/")
public class ProjectController {
  private final ProjectService projectService;

  @PostMapping("create")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @Operation(summary = "create project")
  ResponseEntity<Long> createProject(
      @Validated @RequestBody CreateProjectRequest createProjectRequest) {
    return ResponseEntity.status(CREATED).body(projectService.createProject(createProjectRequest));
  }

  @GetMapping
  @Operation(summary = "project list")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER','ROLE_TEAM_MEMBER')")
  ResponseEntity<List<ProjectResponse>> projectList() {
    return ResponseEntity.ok(projectService.listProject());
  }

  @GetMapping("{projectId}")
  @Operation(summary = "get project id")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER','ROLE_TEAM_MEMBER')")
  ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long projectId) {
    return ResponseEntity.ok(projectService.getProjectById(projectId));
  }

  @GetMapping("{projectId}/invite-user/{userId}")
  @Operation(summary = "add contributor to the project")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ResponseEntity<String> projectAddContributor(
      @PathVariable Long projectId, @PathVariable Long userId) {
    return ResponseEntity.ok(projectService.addContributor(projectId, userId));
  }
}
