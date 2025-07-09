package com.example.Aston_traine3.controller;

import com.example.Aston_traine3.AstonTraine3Application;
import com.example.Aston_traine3.dto.EmailRequestDTO;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = AstonTraine3Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class NotificationControllerTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @DynamicPropertySource
    static void configureMailProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
        registry.add("spring.mail.username", () -> "user");
        registry.add("spring.mail.password", () -> "admin");
        registry.add("mail.from", () -> "test@example.com");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSendEmailViaApi() throws Exception {
        EmailRequestDTO request = new EmailRequestDTO();
        request.setEmail("recipient@example.com");
        request.setSubject("Test Subject");
        request.setMessage("Test Message");

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/notifications/email",
                request,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Проверяем получение письма
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage message = receivedMessages[0];
        assertEquals("Test Subject", message.getSubject());
        assertTrue(String.valueOf(message.getContent()).contains("Test Message"));
        assertEquals("recipient@example.com", message.getAllRecipients()[0].toString());
    }
}