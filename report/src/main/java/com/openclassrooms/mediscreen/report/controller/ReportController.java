package com.openclassrooms.mediscreen.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Patient risk assessment reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{patientId}")
    @Operation(
            summary = "Get patient risk assessment report",
            description =
                    "Retrieve a comprehensive diabetes risk assessment report for a specific patient based on their medical notes")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Report successfully retrieved",
                        content = @Content(schema = @Schema(implementation = ReportDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Patient not found",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid patient ID",
                        content = @Content)
            })
    public ResponseEntity<ReportDTO> getPatientRiskReport(
            @PathVariable @Parameter(description = "Patient ID", example = "1", required = true)
                    Long patientId) {
        try {
            ReportDTO report = reportService.getReportOfRisk(patientId);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException _) {
            return ResponseEntity.notFound().build();
        }
    }
}
