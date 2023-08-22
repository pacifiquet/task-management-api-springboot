package com.taskhero.user.service;

import static com.taskhero.user.security.UserPermission.PROJECT_DELETE;
import static com.taskhero.user.security.UserPermission.PROJECT_READ;
import static com.taskhero.user.security.UserPermission.PROJECT_UPDATE;
import static com.taskhero.user.security.UserPermission.PROJECT_WRITE;
import static com.taskhero.user.security.UserPermission.TASK_DELETE;
import static com.taskhero.user.security.UserPermission.TASK_READ;
import static com.taskhero.user.security.UserPermission.TASK_UPDATE;
import static com.taskhero.user.security.UserPermission.TASK_WRITE;
import static com.taskhero.user.security.UserRole.ROLE_ADMIN;
import static com.taskhero.user.security.UserRole.ROLE_MANAGER;
import static com.taskhero.user.security.UserRole.ROLE_TEAM_MEMBER;
import static com.taskhero.user.security.UserRole.ROLE_USER;

import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.PermissionRepository;
import com.taskhero.user.repository.UserRoleRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public record UserRoleAndPermissionHandler(
    PermissionRepository permissionRepository, UserRoleRepository roleRepository)
    implements IUserRoleAndPermissionHandler {

  @Override
  public void extractPermissions(Set<String> permissions, User user) {
    Set<Permission> permissionSet = new HashSet<>();
    if (permissions.isEmpty()) {
      permissionSet.addAll(
          Set.of(
              permissionRepository.findByUserPermissions(PROJECT_UPDATE).orElseThrow(),
              permissionRepository.findByUserPermissions(PROJECT_DELETE).orElseThrow(),
              permissionRepository.findByUserPermissions(PROJECT_WRITE).orElseThrow(),
              permissionRepository.findByUserPermissions(PROJECT_READ).orElseThrow(),
              permissionRepository.findByUserPermissions(TASK_DELETE).orElseThrow(),
              permissionRepository.findByUserPermissions(TASK_UPDATE).orElseThrow(),
              permissionRepository.findByUserPermissions(TASK_WRITE).orElseThrow(),
              permissionRepository.findByUserPermissions(TASK_READ).orElseThrow()));
    } else {
      permissions.forEach(
          p -> {
            switch (p) {
              case "pw" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(PROJECT_WRITE).orElseThrow());
              case "pd" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(PROJECT_DELETE).orElseThrow());
              case "pu" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(PROJECT_UPDATE).orElseThrow());
              case "pr" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(PROJECT_READ).orElseThrow());
              case "tw" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(TASK_WRITE).orElseThrow());
              case "td" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(TASK_DELETE).orElseThrow());
              case "tu" -> permissionSet.add(
                  permissionRepository.findByUserPermissions(TASK_UPDATE).orElseThrow());
              default -> permissionSet.add(
                  permissionRepository.findByUserPermissions(TASK_READ).orElseThrow());
            }
          });
    }
    user.setUserPermissions(permissionSet);
  }

  @Override
  public void extractRoles(Set<String> roles, User user) {
    Set<Role> roleSet = new HashSet<>();
    if (roles.isEmpty()) {
      roleSet.add(roleRepository.findByUserRole(ROLE_ADMIN).orElseThrow());
    } else {
      roles.forEach(
          r -> {
            if (r.equalsIgnoreCase("admin")) {
              roleSet.add(roleRepository.findByUserRole(ROLE_ADMIN).orElseThrow());
            }
            if (r.equalsIgnoreCase("manager")) {
              roleSet.add(roleRepository.findByUserRole(ROLE_MANAGER).orElseThrow());
            }
            if (r.equalsIgnoreCase("user")) {
              roleSet.add(roleRepository.findByUserRole(ROLE_USER).orElseThrow());
            }

            if (r.equalsIgnoreCase("teamMember")) {
              roleSet.add(roleRepository.findByUserRole(ROLE_TEAM_MEMBER).orElseThrow());
            }
          });
    }
    user.setRoles(roleSet);
  }
}
