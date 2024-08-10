package org.example.task_manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user, PasswordEncoder passwordEncoder) {
        Optional<User> isUserExists = userRepository.findByUsername(user.getUsername());
        if (isUserExists.isEmpty()) {
            // Hashing passwords
            // BCrypt uses salt so the same passwords can give different results
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(hashedPassword);
            user.setPasswordConfirmation(hashedPassword);

            userRepository.save(user);
        } else {
            log.debug("User already exists");
        }
    }

    @Override
    public void generateConfirmationCode(User user) {
        Random random = new Random();

        user.setCode(String.valueOf(random.nextInt(1000, 9999)));
        user.setConfirmCodeEnabled(false);

        userRepository.save(user);
    }

    @Override
    public boolean verifyConfirmationCode(User user, String confirmationCode) {
        return user.getCode().equals(confirmationCode);
    }

    @Override
    public boolean isCodeExpired(User user) {
        LocalDateTime curDateTime = LocalDateTime.now();
        LocalDateTime dateInServer = user.getDateCodeConfirmed();

        long seconds = ChronoUnit.SECONDS.between(dateInServer, curDateTime);
        return seconds > User.EXPIRATION_TIME;
    }

    @Override
    public void acceptUser(User user) {
        user.setConfirmCodeEnabled(true);
        user.setDateCodeConfirmed(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void resetCodeConfirmation(User user) {
        user.setConfirmCodeEnabled(false);
        user.setDateCodeConfirmed(null);

        userRepository.save(user);
    }
}
