package com.personalfinancetracker.controller;

import com.personalfinancetracker.dto.BudgetDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetDTO budgetDTO, Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(budgetService.createBudget(userId, budgetDTO));
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getUserBudgets(Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Budget> updateBudget(@PathVariable String budgetId, @RequestBody BudgetDTO budgetDTO) {
        return ResponseEntity.ok(budgetService.updateBudget(budgetId, budgetDTO));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable String budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.ok("Budget deleted successfully");
    }
}
