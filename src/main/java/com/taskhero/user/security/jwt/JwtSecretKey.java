package com.taskhero.user.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JwtSecretKey {
  private final JwtConfig jwtConfig;

  @Bean
  public SecretKey secretKey() {
    byte[] decode = Decoders.BASE64.decode(jwtConfig.getSecretKey());
    return Keys.hmacShaKeyFor(decode);
  }
}
