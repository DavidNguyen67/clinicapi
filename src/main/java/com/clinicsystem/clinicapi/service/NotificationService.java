package com.clinicsystem.clinicapi.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, Object data) {
        log.info("Sending notification to user: {}", userId);

        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", data);
    }

}