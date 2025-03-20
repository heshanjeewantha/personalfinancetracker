package com.personalfinancetracker.controller;

import com.personalfinancetracker.dto.GoalDTO;
import com.personalfinancetracker.model.Goal;
import com.personalfinancetracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    /**
     * Create a new financial goal.
     */
    @PostMapping("/create")
    public ResponseEntity<GoalDTO> createGoal(
            Authentication auth,
            @RequestBody GoalDTO goalDTO) {

        String userId = auth.getName();
        Goal goal = goalService.createGoal(userId, goalDTO.getGoalName(), goalDTO.getGoalTargetAmount(), goalDTO.isAutoAllocate());

        GoalDTO responseDTO = GoalDTO.builder()
                .goalId(goal.getId())
                .goalName(goal.getGoalName())
                .goalTargetAmount(goal.getGoalTargetAmount())
                .goalCurrentSavings(goal.getGoalCurrentSavings())
                .autoAllocate(goal.isAutoAllocate())
                .isCompleted(goal.isCompleted())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Update progress towards a financial goal.
     */
    @PostMapping("/{goalId}/progress")
    public ResponseEntity<GoalDTO> updateGoalProgress(
            Authentication auth,
            @PathVariable String goalId,
            @RequestParam double progressAmount) {

        String userId = auth.getName();
        Goal updatedGoal = goalService.updateGoalProgress(userId, goalId, progressAmount);

        GoalDTO responseDTO = GoalDTO.builder()
                .goalId(updatedGoal.getId())
                .goalName(updatedGoal.getGoalName())
                .goalTargetAmount(updatedGoal.getGoalTargetAmount())
                .goalCurrentSavings(updatedGoal.getGoalCurrentSavings())
                .autoAllocate(updatedGoal.isAutoAllocate())
                .isCompleted(updatedGoal.isCompleted())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Allocate savings from income to a goal.
     */
    @PostMapping("/{goalId}/allocate-savings")
    public ResponseEntity<GoalDTO> allocateSavings(
            Authentication auth,
            @PathVariable String goalId,
            @RequestParam double amount) {

        String userId = auth.getName();
        Goal updatedGoal = goalService.allocateSavingsToGoal(userId, goalId, amount);

        GoalDTO responseDTO = GoalDTO.builder()
                .goalId(updatedGoal.getId())
                .goalName(updatedGoal.getGoalName())
                .goalTargetAmount(updatedGoal.getGoalTargetAmount())
                .goalCurrentSavings(updatedGoal.getGoalCurrentSavings())
                .autoAllocate(updatedGoal.isAutoAllocate())
                .isCompleted(updatedGoal.isCompleted())
                .build();

        return ResponseEntity.ok(responseDTO);
    }
}
