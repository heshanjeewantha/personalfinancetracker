package com.personalfinancetracker.repository;

import com.personalfinancetracker.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdAndCategory(String userId, String category);
    List<Transaction> findByUserIdAndTagsContaining(String userId, String tag);
    List<Transaction> findByUserIdAndRecurringTrue(String userId);

}
