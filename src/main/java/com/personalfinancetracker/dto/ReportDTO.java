package com.personalfinancetracker.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private LocalDateTime generatedAt;
    private Map<String, Double> incomeVsExpense;
    private Map<String, Double> categoryWiseSpending;
    private String currency;
}
