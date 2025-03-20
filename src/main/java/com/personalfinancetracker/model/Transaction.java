package com.personalfinancetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String userId;          // Link to User
    private String type;            // INCOME or EXPENSE
    private double amount;
    private String preferredCurrency;
    private String category;
    private String description;
    private List<String> tags;
    private boolean recurring;
    private String recurrencePattern;
    private LocalDateTime date;
    private String budgetId;


    // ✅ Goal-Related Fields
    //private String goalId; // Links to Goal
    //private boolean isGoalAllocation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
