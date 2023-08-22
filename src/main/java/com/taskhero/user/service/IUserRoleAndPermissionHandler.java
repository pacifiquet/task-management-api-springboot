package com.taskhero.user.service;

import com.taskhero.user.models.User;
import java.util.Set;

public interface IUserRoleAndPermissionHandler {
  void extractPermissions(Set<String> permissions, User user);

  void extractRoles(Set<String> roles, User user);
}
