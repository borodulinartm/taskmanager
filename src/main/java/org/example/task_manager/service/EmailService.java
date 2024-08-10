package org.example.task_manager.service;

public interface EmailService {
    void sendEmail(String receiver, String subject, String content);
}
