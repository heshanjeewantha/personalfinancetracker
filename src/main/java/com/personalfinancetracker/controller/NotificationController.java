package com.personalfinancetracker.controller;

import com.personalfinancetracker.model.Notification;
import com.personalfinancetracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for the authenticated user.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {

        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    /**
     * Mark a notification as read.
     */
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read.");
    }
    // **New: Send a Notification**
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification) {
        notificationService.sendNotification(notification.getUserId(), notification.getMessage());
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
