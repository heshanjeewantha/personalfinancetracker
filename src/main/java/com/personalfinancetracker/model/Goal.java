package com.personalfinancetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "goals")
public class Goal {
    @Id
    private String id;
    private String userId;
    private String goalName;
    private double goalTargetAmount;
    private double goalCurrentSavings;
    private boolean autoAllocate;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setIsCompleted(boolean b) {
    }
}
