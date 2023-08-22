package com.taskhero.user.security;

import lombok.Getter;

@Getter
public enum UserPermission {
  PROJECT_READ("project:read"),
  PROJECT_UPDATE("project:update"),
  PROJECT_WRITE("project:write"),
  PROJECT_DELETE("project:delete"),
  TASK_READ("task:read"),
  TASK_UPDATE("task:update"),
  TASK_WRITE("task:write"),
  TASK_DELETE("task:delete");

  private final String permission;

  UserPermission(String permission) {
    this.permission = permission;
  }
}
