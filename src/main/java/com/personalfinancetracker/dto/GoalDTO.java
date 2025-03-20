package com.personalfinancetracker.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDTO {
    private String goalId;
    private String goalName;
    private double goalTargetAmount;
    private double goalCurrentSavings;
    private boolean autoAllocate;
    private boolean isCompleted;
}
