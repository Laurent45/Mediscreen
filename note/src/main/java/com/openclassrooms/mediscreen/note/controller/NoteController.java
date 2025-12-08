package com.openclassrooms.mediscreen.note.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mediscreen.note.controller.dto.NoteRequestDto;
import com.openclassrooms.mediscreen.note.controller.dto.NoteResponseDto;
import com.openclassrooms.mediscreen.note.controller.dto.UpdateReportRequestDto;
import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.service.NoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
@Tag(name = "Note", description = "CRUD operations for practitioner notes management")
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "Retrieve all notes")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "List of all notes returned")
            })
    @GetMapping
    public List<NoteResponseDto> getAllNotes() {
        return noteService.findAll().stream().map(NoteResponseDto::from).toList();
    }

    @Operation(summary = "Retrieve a note by ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Note found and returned"),
                @ApiResponse(responseCode = "404", description = "Note not found")
            })
    @GetMapping("/{id}")
    public NoteResponseDto getNoteById(@PathVariable String id) {
        return NoteResponseDto.from(noteService.findById(id));
    }

    @Operation(summary = "Retrieve all notes for a patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "List of patient notes returned")
            })
    @GetMapping("/patient/{patientId}")
    public List<NoteResponseDto> getNotesByPatientId(@PathVariable Long patientId) {
        return noteService.findByPatientId(patientId).stream().map(NoteResponseDto::from).toList();
    }

    @Operation(summary = "Create a new note")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Note created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request body")
            })
    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(
            @RequestBody @Valid NoteRequestDto noteRequest) {
        Note savedNote = noteService.create(noteRequest.toEntity());
        NoteResponseDto response = NoteResponseDto.from(savedNote);
        return ResponseEntity.created(URI.create("/api/v1/notes/" + savedNote.getId()))
                .body(response);
    }

    @Operation(summary = "Update a note report")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Note updated successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request body"),
                @ApiResponse(responseCode = "404", description = "Note not found")
            })
    @PutMapping("/{id}")
    public NoteResponseDto updateNoteReport(
            @PathVariable String id, @RequestBody @Valid UpdateReportRequestDto updateRequest) {
        Note updatedNote = noteService.updateReport(id, updateRequest.report());
        return NoteResponseDto.from(updatedNote);
    }

    @Operation(summary = "Delete a note")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Note deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Note not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
