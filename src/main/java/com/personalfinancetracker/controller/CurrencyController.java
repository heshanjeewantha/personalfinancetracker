package com.personalfinancetracker.controller;



import com.personalfinancetracker.service.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyConverter currencyConverter;

    /**
     * Convert currency from one type to another.
     */
    @GetMapping("/convert")
    public ResponseEntity<Double> convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {

        double convertedAmount = currencyConverter.convertCurrency(from, to, amount);
        return ResponseEntity.ok(convertedAmount);
    }
}
