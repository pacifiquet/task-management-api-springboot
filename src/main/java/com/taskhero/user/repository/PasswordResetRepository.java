package com.taskhero.user.repository;

import com.taskhero.user.models.PasswordResetToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByPasswordToken(String token);
}
