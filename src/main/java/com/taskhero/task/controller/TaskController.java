package com.taskhero.task.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.taskhero.task.dto.CreateTaskRequest;
import com.taskhero.task.dto.ProjectTaskResponse;
import com.taskhero.task.service.ProjectTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/{projectId}/tasks/")
@AllArgsConstructor
@Tag(name = "Project Task Controller")
public class TaskController {
  private final ProjectTaskService projectTaskService;

  @PostMapping("add")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
  @Operation(summary = "add task project")
  ResponseEntity<Long> addTaskProject(
      @Validated @RequestBody CreateTaskRequest createTaskRequest,
      @PathVariable("projectId") Long projectId) {
    return ResponseEntity.status(CREATED)
        .body(projectTaskService.addTaskProject(createTaskRequest, projectId));
  }

  @PatchMapping("assign/{taskId}/{userId}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
  @Operation(summary = "assign user to task")
  ResponseEntity<String> assignUserToTask(
      @PathVariable("projectId") Long projectId,
      @PathVariable("userId") Long userId,
      @PathVariable Long taskId) {
    return ResponseEntity.ok(projectTaskService.assignUserToTask(userId, taskId));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MANAGER','ROLE_TEAM_MEMBER')")
  @Operation(summary = "task list")
  ResponseEntity<List<ProjectTaskResponse>> taskList(@PathVariable Long projectId) {
    return ResponseEntity.ok(projectTaskService.taskList(projectId));
  }

  @GetMapping("{taskId}")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MANAGER','ROLE_TEAM_MEMBER')")
  @Operation(summary = "get task by Id")
  ResponseEntity<ProjectTaskResponse> getTaskProjectById(
      @PathVariable("taskId") Long tasKId, @PathVariable Long projectId) {
    return ResponseEntity.ok(projectTaskService.getTaskProjectById(tasKId, projectId));
  }
}
