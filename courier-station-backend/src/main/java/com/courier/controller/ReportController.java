package com.courier.controller;

import com.courier.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/daily")
    public Map<String, Object> getDailyReport(@RequestParam(value = "date", required = false) String dateStr) {
        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        return reportService.getDailyReport(date);
    }
}