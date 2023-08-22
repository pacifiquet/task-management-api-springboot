package com.taskhero.user.service;

import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
  LoginResponse authenticate(LoginRequest loginRequest);

  void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IllegalArgumentException, IOException;
}
