package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.TransactionDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.model.Transaction;
import com.personalfinancetracker.repository.BudgetRepository;
import com.personalfinancetracker.repository.TransactionRepository;
import com.personalfinancetracker.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final NotificationUtil notificationUtil;

    @Override
    public Transaction createTransaction(String userId, TransactionDTO transactionDTO) {
        List<Budget> budgets = budgetRepository.findByUserIdAndCategory(userId, transactionDTO.getCategory());

        String budgetId = null;
        String notificationMessage = null;

        if (!budgets.isEmpty()) {
            Budget budget = budgets.get(0);
            budgetId = budget.getId();

            // Update current spending
            double newSpending = budget.getCurrentSpending() + transactionDTO.getAmount();
            budget.setCurrentSpending(newSpending);
            budget.setUpdatedAt(LocalDateTime.now());

            // Check for nearing or exceeding limits
            double spendingPercent = (newSpending / budget.getAmount()) * 100;

            if (spendingPercent >= 90 && spendingPercent < 100 && !budget.isNearingLimit()) {
                notificationMessage = "⚠️ You are nearing your budget for " + budget.getCategory();
                budget.setNearingLimit(true);
            }

            if (spendingPercent >= 100 && !budget.isExceeded()) {
                notificationMessage = "🚨 You have exceeded your budget for " + budget.getCategory();
                budget.setExceeded(true);
            }

            budgetRepository.save(budget);
        }

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .type(transactionDTO.getType())
                .amount(transactionDTO.getAmount())
                .preferredCurrency(transactionDTO.getPreferredCurrency())
                .category(transactionDTO.getCategory())
                .description(transactionDTO.getDescription())
                .tags(transactionDTO.getTags())
                .recurring(transactionDTO.isRecurring())
                .recurrencePattern(transactionDTO.getRecurrencePattern())
                .date(transactionDTO.getDate())
                .budgetId(budgetId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // ✅ Return the message in API response
        return transaction;
    }



    @Override
    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public Transaction updateTransaction(String transactionId, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setType(transactionDTO.getType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPreferredCurrency(transactionDTO.getPreferredCurrency());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setTags(transactionDTO.getTags());
        transaction.setRecurring(transactionDTO.isRecurring());
        transaction.setRecurrencePattern(transactionDTO.getRecurrencePattern());
        transaction.setDate(transactionDTO.getDate());
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Update budget's current spending
        if (transaction.getBudgetId() != null) {
            Budget budget = budgetRepository.findById(transaction.getBudgetId())
                    .orElseThrow(() -> new RuntimeException("Budget not found"));

            budget.setCurrentSpending(budget.getCurrentSpending() - transaction.getAmount());
            budget.setUpdatedAt(LocalDateTime.now());
            budgetRepository.save(budget);
        }

        transactionRepository.deleteById(transactionId);
    }


    @Override
    public List<Transaction> getTransactionsByCategory(String userId, String category) {
        return transactionRepository.findByUserIdAndCategory(userId, category);
    }

    @Override
    public List<Transaction> getTransactionsByTag(String userId, String tag) {
        return transactionRepository.findByUserIdAndTagsContaining(userId, tag);
    }

    @Override
    public List<Transaction> getRecurringTransactions(String userId) {
        return transactionRepository.findByUserIdAndRecurringTrue(userId);
    }


}
