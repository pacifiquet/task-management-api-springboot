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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Project Controller")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
@RequestMapping("/api/v1/{userId}/projects")
public class ProjectController {
  private final ProjectService projectService;

  @PostMapping("/create")
  @Operation(summary = "create project")
  ResponseEntity<Long> createProject(
      @Validated @RequestBody CreateProjectRequest createProjectRequest,
      @PathVariable("userId") Long userId) {
    return ResponseEntity.status(CREATED)
        .body(projectService.createProject(createProjectRequest, userId));
  }

  @GetMapping
  @Operation(summary = "project list")
  ResponseEntity<List<ProjectResponse>> projectList(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(projectService.listProject(userId));
  }

  @GetMapping("{projectId}")
  @Operation(summary = "get project id")
  ResponseEntity<ProjectResponse> getProjectById(
      @PathVariable("userId") Long userId, @PathVariable Long projectId) {
    return ResponseEntity.ok(projectService.getProjectById(userId, projectId));
  }

  @GetMapping("{projectId}/invite-user")
  @Operation(summary = "Invite a user to the project")
  ResponseEntity<String> projectInviteUser(
      @RequestParam("email") String email,
      @PathVariable Long projectId,
      @PathVariable Long userId) {
    return ResponseEntity.ok(projectService.inviteUser(userId, projectId, email));
  }
}
