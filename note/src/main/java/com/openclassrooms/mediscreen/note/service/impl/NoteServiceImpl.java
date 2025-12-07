package com.openclassrooms.mediscreen.note.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.mediscreen.note.exception.NotFoundNoteException;
import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.repository.NoteRepository;
import com.openclassrooms.mediscreen.note.service.INoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<Note> getAllNote() {
        return noteRepository.findAll();
    }

    @Override
    public Note getNoteById(String id) {
        return getNote(id);
    }

    @Override
    public List<Note> getAllNoteOfPatientId(Long patientId) {
        return noteRepository.findNoteByPatientId(patientId);
    }

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note updateReport(String id, String report) {
        Note note = getNote(id);
        if (report != null) {
            note.setReport(report);
        }
        noteRepository.save(note);
        return note;
    }

    @Override
    public void removeNote(String id) {
        Note note = getNote(id);
        noteRepository.delete(note);
    }

    private Note getNote(String id) {
        return noteRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundNoteException("id document not" + " found: " + id));
    }
}
