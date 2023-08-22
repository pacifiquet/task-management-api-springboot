package com.taskhero.user.models;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class VerificationToken {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String token;
  private Date expirationTime;

  @OneToOne(fetch = EAGER)
  @JoinColumn(
      foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"),
      nullable = false,
      name = "user_id")
  private User user;
}
