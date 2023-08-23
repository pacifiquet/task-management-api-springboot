package com.taskhero.project.models;

import com.taskhero.user.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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

  @ManyToOne(fetch = FetchType.EAGER)
  private User owner;

  @OneToMany private final List<User> contributors = new ArrayList<>();
}
