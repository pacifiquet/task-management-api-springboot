package com.taskhero.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
@Configuration
public class CsrfTokenAddingFilter extends OncePerRequestFilter {
  private final CsrfTokenRepository csrfTokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
    if (csrfToken != null) {
      request.setAttribute(CsrfToken.class.getName(), csrfToken);
    }
    filterChain.doFilter(request, response);
  }
}
