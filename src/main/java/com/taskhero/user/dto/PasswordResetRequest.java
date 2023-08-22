package com.taskhero.user.dto;

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
public class PasswordResetRequest {
  private String email;
  private String oldPassword;
  private String newPassword;
}
