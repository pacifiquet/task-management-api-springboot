package com.taskhero.user.security.jwt;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
  String extractUsername(String token);

  <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

  String generateToken(UserDetails userDetails);

  String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

  String generateRefreshToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails);

  default boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new java.util.Date());
  }

  default Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
