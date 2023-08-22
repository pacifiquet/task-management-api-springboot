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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String passwordToken;
  private Date expirationTime;

  @OneToOne(fetch = EAGER)
  @JoinColumn(
      foreignKey = @ForeignKey(name = "FK_RESET_PASSWORD_TOKEN"),
      nullable = false,
      name = "user_id")
  private User user;
}
