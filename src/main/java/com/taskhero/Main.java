package com.taskhero;

import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.repository.PermissionRepository;
import com.taskhero.user.repository.UserRoleRepository;
import com.taskhero.user.security.UserPermission;
import com.taskhero.user.security.UserRole;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      UserRoleRepository userRoleRepository, PermissionRepository permissionRepository) {
    return args -> {
      if (userRoleRepository.findAll().isEmpty())
        userRoleRepository.saveAll(
            List.of(
                Role.builder().userRole(UserRole.ROLE_ADMIN).build(),
                Role.builder().userRole(UserRole.ROLE_USER).build(),
                Role.builder().userRole(UserRole.ROLE_TEAM_MEMBER).build(),
                Role.builder().userRole(UserRole.ROLE_MANAGER).build()));

      if (permissionRepository.findAll().isEmpty())
        permissionRepository.saveAll(
            List.of(
                Permission.builder().userPermissions(UserPermission.PROJECT_DELETE).build(),
                Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build(),
                Permission.builder().userPermissions(UserPermission.PROJECT_UPDATE).build(),
                Permission.builder().userPermissions(UserPermission.PROJECT_READ).build(),
                Permission.builder().userPermissions(UserPermission.TASK_WRITE).build(),
                Permission.builder().userPermissions(UserPermission.TASK_UPDATE).build(),
                Permission.builder().userPermissions(UserPermission.TASK_DELETE).build(),
                Permission.builder().userPermissions(UserPermission.TASK_READ).build()));
    };
  }
}
