package com.taskhero.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskhero.user.dto.LoginRequest;
import com.taskhero.user.dto.LoginResponse;
import com.taskhero.user.models.AuthJwtToken;
import com.taskhero.user.models.User;
import com.taskhero.user.repository.TokenRepository;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.security.jwt.IJwtService;
import com.taskhero.user.security.jwt.TokenType;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final AuthenticationManager authenticationManager;
  private final IJwtService jwtService;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  public LoginResponse authenticate(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    if (user.isEnabled()) {
      UserDetails userDetails = UserDetailsImpl.build(user);
      String jwtToken = jwtService.generateToken(userDetails);
      String refreshToken = jwtService.generateRefreshToken(userDetails);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      return LoginResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    } else {
      throw new IllegalStateException("account is disabled");
    }
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IllegalArgumentException, IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = userRepository.findByEmail(userEmail).orElseThrow();
      UserDetails userDetails = UserDetailsImpl.build(user);
      if (jwtService.isTokenValid(refreshToken, userDetails)) {
        String accessToken = jwtService.generateToken(userDetails);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        LoginResponse loginResponse =
            LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
      }
    }
  }

  private void saveUserToken(User user, String jwtToken) {
    AuthJwtToken token =
        AuthJwtToken.builder()
            .user(user)
            .jwtToken(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    List<AuthJwtToken> allValidAuthJwtTokenByUser = tokenRepository.findAllValidTokenByUser(user);
    if (allValidAuthJwtTokenByUser.isEmpty()) return;
    allValidAuthJwtTokenByUser.forEach(
        authJwtToken -> {
          authJwtToken.setRevoked(true);
          authJwtToken.setExpired(true);
        });
    tokenRepository.saveAll(allValidAuthJwtTokenByUser);
  }
}
