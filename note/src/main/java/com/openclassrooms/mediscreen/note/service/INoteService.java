package com.openclassrooms.mediscreen.note.service;

import com.openclassrooms.mediscreen.note.model.Note;

import java.util.List;

public interface INoteService {

    /**
     *
     * @return
     */
    List<Note> getAllNote();

    /**
     *
     * @param id
     * @return
     */
    Note getNoteById(String id);

    /**
     *
     * @param patientId
     * @return
     */
    List<Note> getAllNoteOfPatientId(Long patientId);

    /**
     *
     * @param note
     * @return
     */
    Note createNote(Note note);

    /**
     *
     * @param id
     * @param report
     * @return
     */
    Note updateReport(String id, String report);

    /**
     *
     * @param id
     */
    void removeNote(String id);
}
