package com.taskhero.task.service;

import com.taskhero.task.dto.CreateTaskRequest;
import com.taskhero.task.dto.ProjectTaskResponse;
import java.util.List;

public interface ProjectTaskService {
  Long addTaskProject(CreateTaskRequest createTaskRequest, Long projectId);

  String assignUserToTask(Long userId, Long task, Long taskId);

  List<ProjectTaskResponse> taskList(Long projectId);

  ProjectTaskResponse getTaskProjectById(Long tasKId, Long projectId);
}
