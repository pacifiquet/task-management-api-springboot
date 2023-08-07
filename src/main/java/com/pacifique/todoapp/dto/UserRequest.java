package com.pacifique.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotNull(message = "user full name is required")
    @NotBlank
    private String fullName;

    @NotNull(message = "user email is required")
    @NotBlank
    private String email;

    @NotNull(message = "user role is required")
    @NotBlank
    private String role;
}
