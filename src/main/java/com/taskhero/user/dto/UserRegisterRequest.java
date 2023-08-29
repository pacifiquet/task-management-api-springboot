package com.taskhero.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taskhero.customervalidation.PasswordMatches;
import com.taskhero.customervalidation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
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
@PasswordMatches
public class UserRegisterRequest {
  @NotBlank(message = "first name is required")
  @JsonProperty("first_name")
  private String firstName;

  @NotBlank(message = "last name is required")
  @JsonProperty("last_name")
  private String lastName;

  @NotBlank(message = "user email is required")
  @ValidEmail
  private String email;

  @NotBlank(message = "password is required")
  private String password;

  @NotBlank(message = "matchPassword is required")
  private String matchPassword;

  private Set<String> roles;

  private Set<String> permissions;
}
