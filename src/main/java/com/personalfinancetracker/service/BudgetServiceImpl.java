package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.BudgetDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.repository.BudgetRepository;
import com.personalfinancetracker.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final NotificationUtil notificationUtil;

    @Override
    public Budget createBudget(String userId, BudgetDTO budgetDTO) {
        if (budgetDTO.getCategory() == null || budgetDTO.getCategory().trim().isEmpty()) {
            budgetDTO.setCategory("General"); // Assign a default category if none is provided
        }

        Budget budget = Budget.builder()
                .userId(userId)
                .amount(budgetDTO.getAmount())
                .currentSpending(0)
                .category(budgetDTO.getCategory())
                .startDate(budgetDTO.getStartDate())
                .endDate(budgetDTO.getEndDate())
                .isExceeded(false)
                .nearingLimit(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getUserBudgets(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public Budget updateBudget(String budgetId, BudgetDTO budgetDTO) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budget.setAmount(budgetDTO.getAmount());
        budget.setCategory(budgetDTO.getCategory());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());
        budget.setUpdatedAt(LocalDateTime.now());

        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(String budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    @Override
    public void updateSpending(String userId, String category, double amount) {
        List<Budget> budgets = budgetRepository.findByUserIdAndCategory(userId, category);

        for (Budget budget : budgets) {
            double newSpending = budget.getCurrentSpending() + amount;
            budget.setCurrentSpending(newSpending);

            // Check if the budget is nearing its limit or has been exceeded
            if (newSpending >= budget.getAmount()) {
                budget.setExceeded(true);
                notificationUtil.sendNotification(userId, "Budget exceeded for category: " + category);
            } else if (newSpending >= budget.getAmount() * 0.8) {
                budget.setNearingLimit(true);
                notificationUtil.sendNotification(userId, "Budget nearing limit for category: " + category);
            }

            budget.setUpdatedAt(LocalDateTime.now());
            budgetRepository.save(budget);
        }
    }
}