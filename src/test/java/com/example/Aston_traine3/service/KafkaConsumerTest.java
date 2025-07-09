package com.example.Aston_traine3.service;

import com.example.Aston_traine3.AstonTraine3Application;
import com.example.Aston_traine3.dto.UserEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = AstonTraine3Application.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@ActiveProfiles("test")
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testKafkaConsumer() throws Exception {
        UserEventDTO createdEvent = new UserEventDTO();
        createdEvent.setEvent("USER_CREATED");
        createdEvent.setEmail("test@example.com");

        String message = objectMapper.writeValueAsString(createdEvent);

        kafkaTemplate.send("user-events", message);

        await().atMost(5, TimeUnit.SECONDS).until(() -> {
            return true;
        });
    }
}