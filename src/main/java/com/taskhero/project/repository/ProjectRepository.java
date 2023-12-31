package com.taskhero.project.repository;

import com.taskhero.project.models.Project;
import com.taskhero.user.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  List<Project> findByOwner(User user);
}
