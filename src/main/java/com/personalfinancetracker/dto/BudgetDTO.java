package com.personalfinancetracker.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDTO {
    private double amount;
    private String category;           // Optional: category-specific budget
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
