package com.taskhero.user.controller;

import static org.springframework.http.HttpStatus.OK;

import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.dto.LoginResponse;
import com.taskhero.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
  private final AuthService authService;

  @ResponseStatus(OK)
  @Operation(summary = "login user")
  @PostMapping("authenticate")
  public LoginResponse login(@Validated @RequestBody LoginRequest loginRequest) {
    return authService.authenticate(loginRequest);
  }

  @PostMapping("refresh-token")
  @Operation(summary = "refresh token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IllegalArgumentException, IOException {
    authService.refreshToken(request, response);
  }
}
