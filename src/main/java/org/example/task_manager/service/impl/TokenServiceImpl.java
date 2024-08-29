package org.example.task_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.Token;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.TokenRepository;
import org.example.task_manager.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(String token, User user) {
        Optional<Token> isToken = tokenRepository.findByUser(user);
        Token myToken;
        if (isToken.isPresent()) {
            myToken = isToken.get();

            myToken.setToken(token);
            myToken.setUser(user);
        } else {
            myToken = Token
                    .builder()
                    .token(token)
                    .user(user)
                    .build();
        }
        tokenRepository.save(myToken);
    }
}
