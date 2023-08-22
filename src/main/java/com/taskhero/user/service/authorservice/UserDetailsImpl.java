package com.taskhero.user.service.authorservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskhero.user.models.User;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode
@AllArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
  @Serial private static final long serialVersionUID = 1L;
  private final Long id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean isEnabled;
  @JsonIgnore private final String password;
  private final LocalDateTime createdAt;
  private final Set<SimpleGrantedAuthority> authorities;

  public static UserDetails build(User user) {
    Set<SimpleGrantedAuthority> authoritySet =
        user.getUserPermissions().stream()
            .map(
                permission ->
                    new SimpleGrantedAuthority(permission.getUserPermissions().getPermission()))
            .collect(Collectors.toSet());
    authoritySet.addAll(
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getUserRole().name()))
            .collect(Collectors.toSet()));

    return new UserDetailsImpl(
        user.getUserId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.isEnabled(),
        user.getPassword(),
        user.getCreatedAt(),
        authoritySet);
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
