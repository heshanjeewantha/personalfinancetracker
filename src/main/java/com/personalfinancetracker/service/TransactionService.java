package com.personalfinancetracker.service;

import com.personalfinancetracker.dto.TransactionDTO;
import com.personalfinancetracker.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(String userId, TransactionDTO transactionDTO);
    List<Transaction> getUserTransactions(String userId);
    Transaction updateTransaction(String transactionId, TransactionDTO transactionDTO);
    void deleteTransaction(String transactionId);
    List<Transaction> getTransactionsByCategory(String userId, String category);
    List<Transaction> getTransactionsByTag(String userId, String tag);
    List<Transaction> getRecurringTransactions(String userId);
}
