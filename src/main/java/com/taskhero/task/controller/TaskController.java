package com.taskhero.task.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/{projectId}/tasks/")
@AllArgsConstructor
@Tag(name = "Project Task Controller")
public class TaskController {
  private final ProjectTaskService projectTaskService;

  @PostMapping("add")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
  @Operation(summary = "add task project")
  ResponseEntity<Long> addTaskProject(
      @Validated @RequestBody CreateTaskRequest createTaskRequest,
      @PathVariable("projectId") Long projectId) {
    return ResponseEntity.status(CREATED)
        .body(projectTaskService.addTaskProject(projectId, createTaskRequest));
  }

  @PatchMapping("assign/{taskId}/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
  @Operation(summary = "assign user to task")
  ResponseEntity<String> assignUserToTask(
      @PathVariable("userId") Long userId,
      @PathVariable("projectId") Long projectId,
      @PathVariable Long taskId) {
    return ResponseEntity.ok(projectTaskService.assignUserToTask(projectId, userId, taskId));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','RUSER','MANAGER','TEAM_MEMBER')")
  @Operation(summary = "task list")
  ResponseEntity<List<ProjectTaskResponse>> taskList(@PathVariable Long projectId) {
    return ResponseEntity.ok(projectTaskService.taskList(projectId));
  }

  @GetMapping("{taskId}")
  @PreAuthorize("hasAnyRole('ADMIN','USER','MANAGER','TEAM_MEMBER')")
  @Operation(summary = "get task by Id")
  ResponseEntity<ProjectTaskResponse> getTaskProjectById(
      @PathVariable Long projectId, @PathVariable("taskId") Long tasKId) {
    return ResponseEntity.ok(projectTaskService.getTaskProjectById(projectId, tasKId));
  }

  @DeleteMapping("{taskId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "delete task")
  @ResponseStatus(NO_CONTENT)
  public void deleteTask(
      @PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
    projectTaskService.deleteTask(projectId, taskId);
  }

  @PutMapping("{taskId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "update task")
  @ResponseStatus(OK)
  public Long updateTask(
      @PathVariable("projectId") Long projectId,
      @PathVariable("taskId") Long taskId,
      @RequestBody @Validated CreateTaskRequest request) {
    return projectTaskService.updateTask(projectId, taskId, request);
  }
}
