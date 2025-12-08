package com.openclassrooms.mediscreen.note.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.mediscreen.note.exception.NoteNotFoundException;
import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.repository.NoteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/** Service for managing note operations. */
@Service
@Log4j2
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    /**
     * Retrieves all notes.
     *
     * @return list of all notes
     */
    public List<Note> findAll() {
        log.debug("Retrieving all notes");
        return noteRepository.findAll();
    }

    /**
     * Retrieves a note by ID.
     *
     * @param id the note ID
     * @return the note
     * @throws NoteNotFoundException if note not found
     */
    public Note findById(String id) {
        log.debug("Retrieving note with id: {}", id);
        return noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
    }

    /**
     * Retrieves all notes for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of notes for the patient
     */
    public List<Note> findByPatientId(Long patientId) {
        log.debug("Retrieving all notes for patient id: {}", patientId);
        return noteRepository.findNoteByPatientId(patientId);
    }

    /**
     * Creates a new note.
     *
     * @param note the note to create
     * @return the created note
     */
    public Note create(Note note) {
        log.debug("Creating note for patient id: {}", note.getPatientId());
        note.setCreated(LocalDateTime.now());
        return noteRepository.save(note);
    }

    /**
     * Updates the report of an existing note.
     *
     * @param id the note ID
     * @param report the new report content
     * @return the updated note
     * @throws NoteNotFoundException if note not found
     */
    public Note updateReport(String id, String report) {
        log.debug("Updating report for note id: {}", id);
        Note note = findById(id);
        note.setReport(report);
        return noteRepository.save(note);
    }

    /**
     * Deletes a note by ID.
     *
     * @param id the note ID
     * @throws NoteNotFoundException if note not found
     */
    public void delete(String id) {
        log.debug("Deleting note with id: {}", id);
        Note note = findById(id);
        noteRepository.delete(note);
    }
}
