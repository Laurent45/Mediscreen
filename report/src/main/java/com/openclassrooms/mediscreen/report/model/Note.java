package com.openclassrooms.mediscreen.report.model;

import java.time.LocalDateTime;

public record Note(
        String id, String practitionerName, Long patientId, String report, LocalDateTime created) {}
