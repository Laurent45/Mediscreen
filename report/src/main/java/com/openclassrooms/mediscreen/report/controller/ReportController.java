package com.openclassrooms.mediscreen.report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reportPatient")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/")
    public ReportDTO getReport(@RequestParam("patientId") Long patientId) {
        return reportService.getReportOfRisk(patientId);
    }
}
