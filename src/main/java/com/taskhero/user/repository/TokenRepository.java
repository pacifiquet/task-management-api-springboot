package com.taskhero.user.repository;

import com.taskhero.user.models.AuthJwtToken;
import com.taskhero.user.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthJwtToken, Long> {
  List<AuthJwtToken> findAllValidTokenByUser(User user);

  Optional<AuthJwtToken> findByJwtToken(String token);
}
