package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.BudgetDTO;
import com.personalfinancetracker.model.Budget;

import java.util.List;

public interface BudgetService {
    Budget createBudget(String userId, BudgetDTO budgetDTO);
    List<Budget> getUserBudgets(String userId);
    Budget updateBudget(String budgetId, BudgetDTO budgetDTO);
    void deleteBudget(String budgetId);
    void updateSpending(String userId, String category, double amount);
}
