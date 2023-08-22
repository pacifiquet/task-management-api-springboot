package com.taskhero.task.models;

import com.taskhero.project.models.Project;
import com.taskhero.user.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private Boolean priority;
  private String status;

  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  private Project project;
}
