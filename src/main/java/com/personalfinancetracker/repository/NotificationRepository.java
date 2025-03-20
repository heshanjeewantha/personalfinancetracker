package com.personalfinancetracker.repository;

import com.personalfinancetracker.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> getNotificationByUserId(String userId);
}
