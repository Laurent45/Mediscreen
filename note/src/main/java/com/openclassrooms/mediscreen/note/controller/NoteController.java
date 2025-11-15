package com.openclassrooms.mediscreen.note.controller;

import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.service.INoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notePatient")
@RequiredArgsConstructor
@Tag(name = "Note", description = "CRUD operations about recommendation's practitioner")
public class NoteController {

    private final INoteService noteService;

    @Operation(summary = "Get all notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All notes return")
    })
    @GetMapping("/allNotes")
    List<Note> getNotes() {
        return noteService.getAllNote();
    }

    @Operation(summary = "Get note by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a note"),
            @ApiResponse(responseCode = "400", description = "Note doesn't exist")
    })
    @GetMapping("/{id}")
    Note getNoteById(@PathVariable("id") String id) {
        return noteService.getNoteById(id);
    }

    @Operation(summary = "Get all notes by a patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all note of patient")
    })
    @GetMapping("/notes/{patientId}")
    List<Note> getNotesByPatientId(@PathVariable("patientId") Long patientId) {
        return noteService.getAllNoteOfPatientId(patientId);
    }

    @Operation(summary = "Create a new note")
    @ApiResponse(responseCode = "201", description = "note is created")
    @PostMapping("/create")
    Note create(@RequestBody Note note) {
        note.setCreated(LocalDateTime.now());
        noteService.createNote(note);
        return note;
    }

    @Operation(summary = "Update a note with a new recommendation")
    @ApiResponse(responseCode = "200", description = "note is updated")
    @PutMapping("/update/{id}")
    Note updateReportById(@PathVariable("id") String id,
                          @RequestParam("report") String report) {
        return noteService.updateReport(id, report);
    }

    @Operation(summary = "Delete a note")
    @DeleteMapping("/remove/{id}")
    void deleteNoteById(@PathVariable("id") String id) {
        noteService.removeNote(id);
    }
}
