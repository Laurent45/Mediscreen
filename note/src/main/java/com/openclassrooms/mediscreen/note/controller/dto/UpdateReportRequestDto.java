package com.openclassrooms.mediscreen.note.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateReportRequestDto(@NotBlank(message = "Report is required") String report) {}
