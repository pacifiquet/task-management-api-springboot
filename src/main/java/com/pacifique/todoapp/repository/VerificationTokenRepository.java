package com.pacifique.todoapp.repository;

import com.pacifique.todoapp.model.VerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
    extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
