package com.personalfinancetracker.repository;

import com.personalfinancetracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUserId(String userId);
    List<Budget> findByUserIdAndCategory(String userId, String category);

}
