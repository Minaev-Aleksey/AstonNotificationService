package com.example.NotificationService.service;

import com.example.NotificationService.dto.UserEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            UserEventDTO event = objectMapper.readValue(message, UserEventDTO.class);

            switch (event.getEvent()) {
                case "USER_CREATED":
                    emailService.sendAccountCreatedEmail(event.getEmail());
                    log.info("Sent account created email to: {}", event.getEmail());
                    break;
                case "USER_DELETED":
                    emailService.sendAccountDeletedEmail(event.getEmail());
                    log.info("Sent account deleted email to: {}", event.getEmail());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEvent());
            }
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", message, e);
        }
    }
}
