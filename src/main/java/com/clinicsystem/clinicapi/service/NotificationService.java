package com.clinicsystem.clinicapi.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, Object data) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", data);
    }

}