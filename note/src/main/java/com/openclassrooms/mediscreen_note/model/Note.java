package com.openclassrooms.mediscreen_note.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Document
public class Note {

    @Id
    @Schema(hidden = true)
    private String id;
    @Field("practitioner_name")
    private String practitionerName;
    @Field(value = "patient_id")
    private Long patientId;
    private String report;
    private LocalDateTime created;

    public Note(String practitionerName, Long patientId, String report,
                LocalDateTime created) {
        this.practitionerName = practitionerName;
        this.patientId = patientId;
        this.report = report;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(practitionerName, note.practitionerName) && Objects.equals(patientId, note.patientId) && Objects.equals(report, note.report) && Objects.equals(created, note.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, practitionerName, patientId, report, created);
    }
}
