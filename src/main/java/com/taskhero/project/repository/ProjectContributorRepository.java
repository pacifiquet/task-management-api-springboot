package com.taskhero.project.repository;

import com.taskhero.project.models.ProjectContributor;
import com.taskhero.user.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectContributorRepository extends JpaRepository<ProjectContributor, Long> {
  List<ProjectContributor> findByUser(User user);
}
