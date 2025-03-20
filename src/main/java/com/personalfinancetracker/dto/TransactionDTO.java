package com.personalfinancetracker.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private String type;              // INCOME or EXPENSE
    private double amount;
    private String preferredCurrency;
    private String category;
    private String description;
    private List<String> tags;
    private boolean recurring;
    private String recurrencePattern; // daily, weekly, monthly
    private LocalDateTime date;


}
