package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.TransactionDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.model.Transaction;
import com.personalfinancetracker.repository.BudgetRepository;
import com.personalfinancetracker.repository.TransactionRepository;
import com.personalfinancetracker.util.NotificationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private NotificationUtil notificationUtil;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDTO transactionDTO;
    private Transaction transaction;
    private Budget budget;

    @BeforeEach
    void setUp() {
        transactionDTO = TransactionDTO.builder()
                .type("EXPENSE")
                .amount(200.0)
                .preferredCurrency("USD")
                .category("Food")
                .description("Dinner")
                .tags(List.of("restaurant"))
                .recurring(false)
                .recurrencePattern(null)
                .date(LocalDateTime.now())
                .build();

        transaction = Transaction.builder()
                .id("txn123")
                .userId("user123")
                .type("EXPENSE")
                .amount(200.0)
                .preferredCurrency("USD")
                .category("Food")
                .description("Dinner")
                .tags(List.of("restaurant"))
                .recurring(false)
                .recurrencePattern(null)
                .date(LocalDateTime.now())
                .budgetId("budget123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        budget = Budget.builder()
                .id("budget123")
                .userId("user123")
                .category("Food")
                .amount(1000.0)
                .currentSpending(500.0)
                .nearingLimit(false)
                .isExceeded(false)
                .build();
    }

    /** Test: Create Transaction */
    @Test
    void testCreateTransaction() {
        when(budgetRepository.findByUserIdAndCategory("user123", "Food")).thenReturn(List.of(budget));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.createTransaction("user123", transactionDTO);

        assertNotNull(savedTransaction);
        assertEquals("EXPENSE", savedTransaction.getType());
        assertEquals(200.0, savedTransaction.getAmount());
        verify(budgetRepository, times(1)).save(any(Budget.class)); // Ensure budget is updated
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    /** ✅ Test: Get All Transactions */
    @Test
    void testGetUserTransactions() {
        when(transactionRepository.findByUserId("user123")).thenReturn(List.of(transaction));

        List<Transaction> transactions = transactionService.getUserTransactions("user123");

        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        assertEquals("Food", transactions.get(0).getCategory());
        verify(transactionRepository, times(1)).findByUserId("user123");
    }

    /** Test: Update Transaction */
    @Test
    void testUpdateTransaction() {
        when(transactionRepository.findById("txn123")).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO updatedDTO = TransactionDTO.builder()
                .type("EXPENSE")
                .amount(250.0)
                .preferredCurrency("USD")
                .category("Groceries")
                .description("Grocery Shopping")
                .tags(List.of("supermarket"))
                .recurring(false)
                .recurrencePattern(null)
                .date(LocalDateTime.now())
                .build();

        Transaction updatedTransaction = transactionService.updateTransaction("txn123", updatedDTO);

        assertNotNull(updatedTransaction);
        assertEquals("Groceries", updatedTransaction.getCategory());
        assertEquals(250.0, updatedTransaction.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    /** Test: Delete Transaction */
    @Test
    void testDeleteTransaction() {
        when(transactionRepository.findById("txn123")).thenReturn(Optional.of(transaction));
        when(budgetRepository.findById("budget123")).thenReturn(Optional.of(budget));

        transactionService.deleteTransaction("txn123");

        verify(transactionRepository, times(1)).deleteById("txn123");
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }
}
