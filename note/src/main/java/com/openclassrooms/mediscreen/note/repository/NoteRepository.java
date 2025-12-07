package com.openclassrooms.mediscreen.note.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mediscreen.note.model.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findNoteByPatientId(Long patientId);
}
