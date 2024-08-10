package org.example.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class EmailConfig {
    @Bean
    public JavaMailSender getSender() throws IOException {
        Properties properties = readProperties();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getProperty("host"));
        mailSender.setUsername(properties.getProperty("username"));
        mailSender.setPassword(properties.getProperty("password"));

        // In memory, it stores in the heap, so I can a
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");

        return mailSender;
    }

    private Properties readProperties() throws IOException {
        Properties props = new Properties();

        File file = ResourceUtils.getFile("classpath:email/email_data.properties");
        try (InputStream in = new FileInputStream(file)) {
            props.load(in);
        }
        return props;
    }
}
