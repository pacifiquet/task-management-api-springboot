package com.taskhero.user.repository;

import com.taskhero.user.models.Permission;
import com.taskhero.user.security.UserPermission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Optional<Permission> findByUserPermissions(UserPermission permissions);
}
