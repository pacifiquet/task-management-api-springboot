package com.pacifique.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "first name is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "last name is required")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "user email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "user role is required")
    private String role;
}
