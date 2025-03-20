package com.personalfinancetracker.service;

import com.personalfinancetracker.model.Goal;
import com.personalfinancetracker.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final NotificationService notificationService;

    /**
     * Create a new financial goal.
     */
    public Goal createGoal(String userId, String goalName, double targetAmount, boolean autoAllocate) {
        Goal goal = Goal.builder()
                .userId(userId)
                .goalName(goalName)
                .goalTargetAmount(targetAmount)
                .goalCurrentSavings(0)
                .autoAllocate(autoAllocate)
                .isCompleted(false)
                .build();

        return goalRepository.save(goal);
    }

    /**
     * Update progress towards a goal.
     */
    @Transactional
    public Goal updateGoalProgress(String userId, String goalId, double progressAmount) {
        Goal goal = goalRepository.findById(goalId)
                .filter(g -> g.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied."));

        goal.setGoalCurrentSavings(goal.getGoalCurrentSavings() + progressAmount);

        if (goal.getGoalCurrentSavings() >= goal.getGoalTargetAmount()) {
            goal.setIsCompleted(true);
        }

        return goalRepository.save(goal);
    }

    /**
     * Allocate savings from income to a goal.
     */
    @Transactional
    public Goal allocateSavingsToGoal(String userId, String goalId, double amount) {
        Goal goal = goalRepository.findById(goalId)
                .filter(g -> g.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied."));

        goal.setGoalCurrentSavings(goal.getGoalCurrentSavings() + amount);

        if (goal.getGoalCurrentSavings() >= goal.getGoalTargetAmount()) {
            goal.setIsCompleted(true);
        }

        return goalRepository.save(goal);
    }
    public void checkGoalDeadlines() {
        List<Goal> allGoals = goalRepository.findAll();

        for (Goal goal : allGoals) {
            double remainingAmount = goal.getGoalTargetAmount() - goal.getGoalCurrentSavings();

            if (remainingAmount > 0 && goal.isAutoAllocate()) {
                notificationService.sendNotification(goal.getUserId(),
                        "⏳ Reminder: You have $" + remainingAmount + " left to save for '" + goal.getGoalName() + "'. Keep going!");
            }
        }
    }
}
