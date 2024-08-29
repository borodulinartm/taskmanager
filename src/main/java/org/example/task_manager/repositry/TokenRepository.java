package org.example.task_manager.repositry;

import org.example.task_manager.models.Token;
import org.example.task_manager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
    List<Token> findAllByUserAndExpiredFalse(User user);
}
