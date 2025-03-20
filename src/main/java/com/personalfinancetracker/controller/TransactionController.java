package com.personalfinancetracker.controller;

import com.personalfinancetracker.dto.TransactionDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.model.Transaction;
import com.personalfinancetracker.repository.BudgetRepository; // ✅ Import BudgetRepository
import com.personalfinancetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final BudgetRepository budgetRepository; // ✅ Inject BudgetRepository

    @PostMapping("/add")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO, Authentication auth) {
        String userId = auth.getName();

        Transaction transaction = transactionService.createTransaction(userId, transactionDTO);

        // Prepare response with transaction and possible notification
        String message = "Transaction created successfully";
        if (transaction.getBudgetId() != null) {
            Budget budget = budgetRepository.findById(transaction.getBudgetId())
                    .orElseThrow(() -> new RuntimeException("Budget not found"));

            double spendingPercent = (budget.getCurrentSpending() / budget.getAmount()) * 100;
            if (spendingPercent >= 90 && spendingPercent < 100) {
                message = "⚠️ You are nearing your budget for " + budget.getCategory();
            } else if (spendingPercent >= 100) {
                message = "🚨 You have exceeded your budget for " + budget.getCategory();
            }
        }

        return ResponseEntity.ok().body(Map.of(
                "transaction", transaction,
                "message", message
        ));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String transactionId, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(transactionId, transactionDTO));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(@PathVariable String category, Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(userId, category));
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Transaction>> getTransactionsByTag(@PathVariable String tag, Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(transactionService.getTransactionsByTag(userId, tag));
    }

    @GetMapping("/recurring")
    public ResponseEntity<List<Transaction>> getRecurringTransactions(Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(transactionService.getRecurringTransactions(userId));
    }
}
