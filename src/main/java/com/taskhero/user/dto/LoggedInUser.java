package com.taskhero.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoggedInUser {
  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  private String email;

  @JsonProperty("create_at")
  private String createAt;

  private Set<SimpleGrantedAuthority> authorities;
}
