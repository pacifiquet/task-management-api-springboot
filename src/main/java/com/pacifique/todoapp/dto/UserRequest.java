package com.pacifique.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "user name is required")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "user email is required")
    private String email;

    @NotBlank(message = "user role is required")
    private String role;
}
