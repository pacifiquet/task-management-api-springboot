package com.taskhero.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class UserResponse {
  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  private String email;
  private boolean enabled;

  @JsonProperty("create_at")
  private String createAt;

  private List<String> roles;
  private List<String> permissions;
}
