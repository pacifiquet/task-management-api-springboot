package com.taskhero.task.service;

import com.taskhero.task.dto.CreateTaskRequest;
import com.taskhero.task.dto.ProjectTaskResponse;
import java.util.List;

public interface ProjectTaskService {
  Long addTaskProject(Long projectId, CreateTaskRequest createTaskRequest);

  String assignUserToTask(Long userId, Long task, Long taskId);

  List<ProjectTaskResponse> taskList(Long projectId);

  ProjectTaskResponse getTaskProjectById(Long projectId, Long tasKId);
}
