package com.openclassrooms.mediscreen.note.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.openclassrooms.mediscreen.note.model.Note;

public record NoteRequestDto(
        @NotBlank(message = "Practitioner name is required") String practitionerName,
        @NotNull(message = "Patient ID is required") Long patientId,
        @NotBlank(message = "Report is required") String report) {

    public Note toEntity() {
        return new Note(practitionerName, patientId, report, null);
    }
}
