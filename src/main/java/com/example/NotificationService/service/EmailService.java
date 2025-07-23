package com.example.NotificationService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    @Value("${mail.site-name}")
    private String siteName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAccountCreatedEmail(String toEmail) {
        String subject = "Welcome to " + siteName;
        String text = "Hello! Your account on the website " + siteName + " was created successfully.";

        sendEmail(toEmail, subject, text);
    }

    public void sendAccountDeletedEmail(String toEmail) {
        String subject = "Your account on the " + siteName + " id deleted";
        String text = "Hello! Your account has been deleted.";

        sendEmail(toEmail, subject, text);
    }

    public void sendCustomEmail(String toEmail, String subject, String text) {
        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
