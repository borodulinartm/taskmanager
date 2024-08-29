package org.example.task_manager.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.Token;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.TokenRepository;
import org.example.task_manager.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(String token, User user) {
        Token myToken = Token
                .builder()
                .token(token)
                .user(user)
                .isExpired(false)
                .build();
        tokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public void saveAll(List<Token> tokens) {
        tokenRepository.saveAll(tokens);
    }

    @Override
    public List<Token> getNonExpiredTokensByUser(User user) {
        return tokenRepository.findAllByUserAndIsExpiredFalse(user);
    }
}
