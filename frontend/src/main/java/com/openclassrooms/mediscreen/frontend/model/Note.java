package com.openclassrooms.mediscreen.frontend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Note {

    private String id;
    private String practitionerName;
    private Long patientId;
    private String report;
    private LocalDateTime created;

    public Note(String practitionerName, Long patientId, String report, LocalDateTime created) {
        this.practitionerName = practitionerName;
        this.patientId = patientId;
        this.report = report;
        this.created = created;
    }
}
