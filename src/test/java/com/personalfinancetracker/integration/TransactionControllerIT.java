package com.personalfinancetracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalfinancetracker.dto.TransactionDTO;
import com.personalfinancetracker.model.Transaction;
import com.personalfinancetracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll(); // Clear data before each test
    }

    /** Test: Create a transaction */
    @Test
    void testCreateTransaction() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .type("EXPENSE")
                .amount(200.0)
                .preferredCurrency("USD")
                .category("Food")
                .description("Dinner")
                .tags(List.of("restaurant"))
                .recurring(false)
                .date(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/transactions/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("EXPENSE"));
    }

    /**  Test: Get All Transactions */
    @Test
    void testGetAllTransactions() throws Exception {
        Transaction transaction = Transaction.builder()
                .userId("user123")
                .type("EXPENSE")
                .amount(100)
                .category("Transport")
                .description("Bus fare")
                .date(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category").value("Transport"));
    }
}
