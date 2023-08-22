package com.taskhero.config.authentication;

import java.util.Objects;

public class TokenAuthenticationService {
  private static TokenAuthenticationService tokenAuthenticationService;

  public TokenAuthenticationService() {
    super();
  }

  public static TokenAuthenticationService getInstance() {
    return Objects.requireNonNullElseGet(
        tokenAuthenticationService, TokenAuthenticationService::new);
  }
}
