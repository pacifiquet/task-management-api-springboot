package com.taskhero.config.authentication;

import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.security.UserPermission;
import com.taskhero.user.security.UserRole;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import com.taskhero.utils.Time;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationHandler {
  private static AuthenticationHandler authenticationHandler;
  private static final Role ROLE = Role.builder().userRole(UserRole.ROLE_ADMIN).build();
  private static final Permission PERMISSION =
      Permission.builder().userPermissions(UserPermission.PROJECT_WRITE).build();
  private static final User VERIFIED_USER =
      User.builder()
          .userId(3L)
          .firstName("peter")
          .lastName("jack")
          .email("peter@gmail.com")
          .password("$2a$10$.2EizqsoJYHpfMY9r30YveaJXmn.P5.jlAFTlSRlehF9QdhRmlLb2") // password
          .roles(Set.of(ROLE))
          .userPermissions(Set.of(PERMISSION))
          .enabled(true)
          .createdAt(Time.currentDateTime())
          .build();

  public AuthenticationHandler() {
    super();
  }

  public static AuthenticationHandler getInstance() {

    return Objects.requireNonNullElseGet(authenticationHandler, AuthenticationHandler::new);
  }

  public void start() {
    UserDetails userDetails = UserDetailsImpl.build(VERIFIED_USER);
    var authentication =
        new UsernamePasswordAuthenticationToken(
            new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                "pacifique",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))),
            null,
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
