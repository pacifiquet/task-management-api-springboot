package com.taskhero.user.repository;

import com.taskhero.user.models.Role;
import com.taskhero.user.security.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByUserRole(UserRole role);
}
