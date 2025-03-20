package com.personalfinancetracker.service;

import com.personalfinancetracker.model.Report;

import java.util.List;

public interface ReportService {
    Report generateReport(String userId);
    List<Report> getUserReports(String userId);
}
