package org.example.task_manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.task_manager.models.User;
import org.example.task_manager.repositry.UserRepository;
import org.example.task_manager.service.EmailService;
import org.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailReceiver;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailReceiver = emailService;
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

        String generatedCode = String.valueOf(random.nextInt(1000, 9999));
        user.setCode(generatedCode);
        user.setConfirmCodeEnabled(false);

        userRepository.save(user);
        sendEmail(user, generatedCode);
    }

    private void sendEmail(User user, String code) {
        // send a message to the user's email
        try {
            File file = ResourceUtils.getFile("classpath:email/email_text.txt");
            String content = Files.readString(file.toPath()) + " " + user.getCode();
            String receiver = user.getEmail();
            String subject = "Activate your account!!!";

            emailReceiver.sendEmail(receiver, subject, content);
        } catch (IOException exception) {
            log.warn("Cannot send message to the email: {}", exception.getMessage());
        }
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
