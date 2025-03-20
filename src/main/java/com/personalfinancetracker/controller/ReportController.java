package com.personalfinancetracker.controller;

import com.personalfinancetracker.dto.ReportDTO;
import com.personalfinancetracker.model.Report;
import com.personalfinancetracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Generate a new financial report for the logged-in user.
     */
    @PostMapping("/generate")
    public ResponseEntity<ReportDTO> generateReport(Authentication auth) {
        String userId = auth.getName();
        Report report = reportService.generateReport(userId);

        // Convert Report entity to ReportDTO
        ReportDTO reportDTO = ReportDTO.builder()
                .generatedAt(report.getGeneratedAt())
                .incomeVsExpense(report.getIncomeVsExpense())
                .categoryWiseSpending(report.getCategoryWiseSpending())
                .currency(report.getCurrency())
                .build();

        return ResponseEntity.ok(reportDTO);
    }

    /**
     * Retrieve all generated reports for the logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports(Authentication auth) {
        String userId = auth.getName();
        List<Report> reports = reportService.getUserReports(userId);

        // Convert each Report entity to ReportDTO
        List<ReportDTO> reportDTOs = reports.stream().map(report -> ReportDTO.builder()
                .generatedAt(report.getGeneratedAt())
                .incomeVsExpense(report.getIncomeVsExpense())
                .categoryWiseSpending(report.getCategoryWiseSpending())
                .currency(report.getCurrency())
                .build()).collect(Collectors.toList());

        return ResponseEntity.ok(reportDTOs);
    }
}
