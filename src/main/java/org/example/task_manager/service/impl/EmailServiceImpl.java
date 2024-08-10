package org.example.task_manager.service.impl;

import jakarta.mail.Message;
import org.example.task_manager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String receiver, String subject, String content) {
        MimeMessagePreparator prep = mimeMessage -> {
            mimeMessage.setRecipients(Message.RecipientType.TO, receiver);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);
        };

        mailSender.send(prep);
    }
}
