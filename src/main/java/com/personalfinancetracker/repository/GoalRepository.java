package com.personalfinancetracker.repository;

import com.personalfinancetracker.model.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends MongoRepository<Goal, String> {
    List<Goal> findByUserId(String userId);
    Optional<Goal> findByUserIdAndGoalName(String userId, String goalName);
}
