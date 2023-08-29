package com.taskhero.project.models;

import com.taskhero.user.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "projectContributors")
public class ProjectContributor {
  @Id
  @Column(updatable = false)
  @SequenceGenerator(
      name = "project_contributor_id_sequence",
      sequenceName = "project_contributor_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_contributor_id_sequence")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @Override
  public String toString() {
    return "ProjectContributor{" + "id=" + id + '}';
  }
}
