package com.personalfinancetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String userId;
    private String message;
    private boolean isRead;  // Mark notification as read/unread
    private LocalDateTime createdAt;


}
