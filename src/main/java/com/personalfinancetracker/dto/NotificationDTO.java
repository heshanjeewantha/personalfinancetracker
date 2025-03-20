package com.personalfinancetracker.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private String id;
    private String userId;
    private String message;
    private boolean isread;  // Changed from isRead to read for better getter naming
    private LocalDateTime createdAt;
}
