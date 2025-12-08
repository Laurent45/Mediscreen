package com.openclassrooms.mediscreen.note.controller.dto;

import java.time.LocalDateTime;

import com.openclassrooms.mediscreen.note.model.Note;

public record NoteResponseDto(
        String id, String practitionerName, Long patientId, String report, LocalDateTime created) {

    public static NoteResponseDto from(Note note) {
        return new NoteResponseDto(
                note.getId(),
                note.getPractitionerName(),
                note.getPatientId(),
                note.getReport(),
                note.getCreated());
    }
}
