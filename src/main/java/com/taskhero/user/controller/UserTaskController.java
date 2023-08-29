package com.taskhero.user.controller;

import com.taskhero.user.dto.UserTaskResponse;
import com.taskhero.user.service.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User Task Controller")
@AllArgsConstructor
@RequestMapping("api/v1/users/tasks")
public class UserTaskController {
  private final UserTaskService userTaskService;

  @GetMapping
  @Operation(summary = "get all user tasks")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER','TEAM_MEMBER')")
  public ResponseEntity<UserTaskResponse> getUserTasks() {
    return ResponseEntity.ok(userTaskService.userTasks());
  }
}
