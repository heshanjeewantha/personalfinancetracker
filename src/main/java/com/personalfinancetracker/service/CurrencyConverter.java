package com.personalfinancetracker.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CurrencyConverter {



        private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

        public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
            RestTemplate restTemplate = new RestTemplate();
            String url = API_URL + fromCurrency;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");

            double rate = rates.getOrDefault(toCurrency, 1.0);
            return amount * rate;
        }

}

