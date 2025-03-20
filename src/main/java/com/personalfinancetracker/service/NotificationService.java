package com.personalfinancetracker.service;

import com.personalfinancetracker.model.Notification;
import com.personalfinancetracker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Send a notification to a user.
     */
    public void sendNotification(String userId, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    /**
     * Get all notifications for a user.
     */
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.getNotificationByUserId(userId);
    }

    /**
     * Mark a notification as read.
     */
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
