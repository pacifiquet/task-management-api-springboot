package com.taskhero.user.security.jwt;

import com.taskhero.user.repository.TokenRepository;
import com.taskhero.user.service.authorservice.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;
  private final TokenRepository tokenRepository;
  private final JwtConfig jwtConfig;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      UserDetailsServiceImpl userDetailsService,
      TokenRepository tokenRepository,
      JwtConfig jwtConfig) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.tokenRepository = tokenRepository;
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader(jwtConfig.getAuthenticationHeader());
    final String jwt;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid =
          tokenRepository
              .findByJwtToken(jwt)
              .map(token -> !token.isExpired() && !token.isRevoked())
              .orElse(false);

      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
