package com.taskhero.user.models;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

import com.taskhero.project.models.Project;
import com.taskhero.project.models.ProjectContributor;
import com.taskhero.task.models.ProjectTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User implements Serializable {
  @Serial private static final long serialVersionUID = 2405172041950251807L;

  @Id
  @SequenceGenerator(
      name = "user_id_sequence",
      sequenceName = "user_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
  private Long userId;

  private String firstName;
  private String lastName;

  @Column(unique = true)
  private String email;

  @Column(length = 60)
  private String password;

  private boolean enabled = false;
  private LocalDateTime createdAt;

  @ManyToMany(fetch = EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(fetch = EAGER)
  @JoinTable(
      name = "user_permissions",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<Permission> userPermissions = new HashSet<>();

  @OneToMany(mappedBy = "owner", fetch = LAZY, orphanRemoval = true, cascade = ALL)
  private final Set<Project> projects = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL, orphanRemoval = true)
  private final Set<ProjectContributor> contributors = new HashSet<>();

  @OneToMany(mappedBy = "assignedTo", fetch = LAZY, orphanRemoval = true, cascade = ALL)
  private final Set<ProjectTask> tasks = new HashSet<>();

  @Override
  public String toString() {
    return "User{" + "userId=" + userId + '}';
  }
}
