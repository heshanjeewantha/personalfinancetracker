package com.personalfinancetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "budgets")
public class Budget {

    @Id
    private String id;

    private String userId;
    private double amount;
    private double currentSpending;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean isExceeded;     // ✅ Ensure this field exists
    private boolean nearingLimit;
    private String recommendation;

   /* // ✅ Goal Tracking Fields
    private String goalName;
    private double goalTargetAmount;
    private double goalCurrentSavings;
    private boolean autoAllocate;*/

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




    public void setIsExceeded(boolean b) {
    }

    public void setExceeded(boolean b) {
    }
}
