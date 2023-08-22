package com.taskhero.task.repository;

import com.taskhero.project.models.Project;
import com.taskhero.task.models.ProjectTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
  List<ProjectTask> findByProject(Project project);
}
