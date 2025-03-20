package com.personalfinancetracker.util;

import org.springframework.stereotype.Component;

@Component
public class NotificationUtil {

    public void sendNotification(String userId, String message) {
        // In real-world, integrate with email/SMS/Push services
        System.out.println("🔔 [Notification to User: " + userId + "] " + message);
    }
}
