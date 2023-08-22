package com.taskhero.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "email is missing") String email,
    @NotBlank(message = "password is missing") String password) {}
