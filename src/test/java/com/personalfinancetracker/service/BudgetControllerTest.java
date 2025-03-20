package com.personalfinancetracker.service;

import com.personalfinancetracker.controller.BudgetController;
import com.personalfinancetracker.dto.BudgetDTO;
import com.personalfinancetracker.model.Budget;
import com.personalfinancetracker.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
    }

    @Test
    void createBudget_ShouldReturnCreatedBudget() throws Exception {
        String userId = "user123";
        // Using a fixed date to avoid timestamp mismatches
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);

        BudgetDTO budgetDTO = new BudgetDTO(1000.0, "Food", startDate, endDate);
        Budget budget = new Budget("1", userId, 1000.0, 0, "Food", startDate, endDate, false, false, "", startDate, endDate);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userId);
        when(budgetService.createBudget(eq(userId), any(BudgetDTO.class))).thenReturn(budget);

        mockMvc.perform(post("/api/budgets")
                        .contentType("application/json")
                        .content("{\"amount\":1000.0, \"category\":\"Food\", \"startDate\":\"2025-03-01T00:00:00\", \"endDate\":\"2025-04-01T00:00:00\"}")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(budget.getId()))
                .andExpect(jsonPath("$.amount").value(budget.getAmount()));
    }

    @Test
    void getUserBudgets_ShouldReturnListOfBudgets() throws Exception {
        String userId = "user123";
        Budget budget1 = new Budget("1", userId, 1000.0, 100, "Food", LocalDateTime.now(), LocalDateTime.now().plusMonths(1), false, false, "", LocalDateTime.now(), LocalDateTime.now());
        Budget budget2 = new Budget("2", userId, 1500.0, 500, "Entertainment", LocalDateTime.now(), LocalDateTime.now().plusMonths(2), true, false, "Limit exceeded", LocalDateTime.now(), LocalDateTime.now());

        List<Budget> budgets = Arrays.asList(budget1, budget2);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userId);
        when(budgetService.getUserBudgets(userId)).thenReturn(budgets);

        mockMvc.perform(get("/api/budgets").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(budget1.getId()))
                .andExpect(jsonPath("$[1].id").value(budget2.getId()));
    }

    @Test
    void updateBudget_ShouldReturnUpdatedBudget() throws Exception {
        String budgetId = "1";
        // Using a fixed date for consistency in the test
        LocalDateTime startDate = LocalDateTime.of(2025, 3, 1, 0, 0, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(2);

        BudgetDTO updatedBudgetDTO = new BudgetDTO(1200.0, "Food", startDate, endDate);
        Budget updatedBudget = new Budget(budgetId, "user123", 1200.0, 200, "Food", startDate, endDate, false, false, "", startDate, endDate);

        when(budgetService.updateBudget(eq(budgetId), any(BudgetDTO.class))).thenReturn(updatedBudget);

        mockMvc.perform(put("/api/budgets/{budgetId}", budgetId)
                        .contentType("application/json")
                        .content("{\"amount\":1200.0, \"category\":\"Food\", \"startDate\":\"2025-03-01T00:00:00\", \"endDate\":\"2025-05-01T00:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(updatedBudget.getAmount()));
    }

    @Test
    void deleteBudget_ShouldReturnSuccessMessage() throws Exception {
        String budgetId = "1";
        doNothing().when(budgetService).deleteBudget(budgetId);

        mockMvc.perform(delete("/api/budgets/{budgetId}", budgetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Budget deleted successfully"));
    }
}
