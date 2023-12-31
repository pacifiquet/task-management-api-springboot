package com.taskhero.task.models;

import com.taskhero.project.models.Project;
import com.taskhero.user.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ProjectTask {
  @Id
  @SequenceGenerator(
      name = "task_id_sequence",
      sequenceName = "task_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_sequence")
  private Long taskId;

  private String name;
  private String description;
  private LocalDate dueDate;

  @Column(columnDefinition = "BOOLEAN")
  private Boolean priority;

  private String status;

  @ManyToOne
  @JoinColumn(name = "assigned_to_id")
  private User assignedTo;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @Override
  public String toString() {
    return "ProjectTask{" + "taskId=" + taskId + '}';
  }
}
