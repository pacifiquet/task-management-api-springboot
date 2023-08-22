package com.taskhero.user.models;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.taskhero.user.security.jwt.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class AuthJwtToken implements Serializable {
  @Serial private static final long serialVersionUID = 1905122041950251207L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String jwtToken;
  private TokenType tokenType = TokenType.BEARER;
  private boolean revoked;
  private boolean expired;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
