package com.personalfinancetracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reports")
public class Report {
    @Id
    private String id;
    private String userId;
    private LocalDateTime generatedAt;
    private Map<String, Double> incomeVsExpense;
    private Map<String, Double> categoryWiseSpending;
    private String currency;
}
