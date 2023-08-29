package com.taskhero.project.models;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import com.taskhero.task.models.ProjectTask;
import com.taskhero.user.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "projects_tb")
public class Project {
  @Id
  @SequenceGenerator(
      name = "project_id_sequence",
      sequenceName = "project_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_sequence")
  private Long projectId;

  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(mappedBy = "project", fetch = LAZY, orphanRemoval = true, cascade = ALL)
  private final List<ProjectContributor> projectContributors = new ArrayList<>();

  @OneToMany(mappedBy = "project", fetch = LAZY, orphanRemoval = true, cascade = ALL)
  private final List<ProjectTask> taskList = new ArrayList<>();

  @Override
  public String toString() {
    return "Project{" + "projectId=" + projectId + '}';
  }
}
