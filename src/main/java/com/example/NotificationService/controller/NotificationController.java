package com.example.NotificationService.controller;

import com.example.NotificationService.dto.EmailRequestDTO;
import com.example.NotificationService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequestDTO request) {
        emailService.sendCustomEmail(request.getEmail(), request.getSubject(), request.getMessage());
        return ResponseEntity.ok().build();
    }
}