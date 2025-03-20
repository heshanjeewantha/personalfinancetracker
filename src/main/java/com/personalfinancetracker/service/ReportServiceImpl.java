package com.personalfinancetracker.service;

import com.personalfinancetracker.model.Report;
import com.personalfinancetracker.model.Transaction;
import com.personalfinancetracker.repository.ReportRepository;
import com.personalfinancetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository transactionRepository;
    private final ReportRepository reportRepository;

    @Override
    public Report generateReport(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        double totalIncome = transactions.stream()
                .filter(txn -> txn.getType().equalsIgnoreCase("INCOME"))
                .mapToDouble(Transaction::getAmount).sum();

        double totalExpense = transactions.stream()
                .filter(txn -> txn.getType().equalsIgnoreCase("EXPENSE"))
                .mapToDouble(Transaction::getAmount).sum();

        Map<String, Double> categorySpending = transactions.stream()
                .filter(txn -> txn.getType().equalsIgnoreCase("EXPENSE"))
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));

        Report report = Report.builder()
                .userId(userId)
                .generatedAt(LocalDateTime.now())
                .incomeVsExpense(Map.of("Income", totalIncome, "Expense", totalExpense))
                .categoryWiseSpending(categorySpending)
                .currency("USD") // Default currency, can be updated later
                .build();

        return reportRepository.save(report);
    }

    @Override
    public List<Report> getUserReports(String userId) {
        return reportRepository.findByUserId(userId);
    }
}
