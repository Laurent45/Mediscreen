package com.openclassrooms.mediscreen.note.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.repository.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock private NoteRepository noteRepositoryMock;

    @InjectMocks private NoteServiceImpl noteServiceUT;

    @BeforeEach
    void setUp() {}

    @Test
    void whenGetAllNote_thenReturnNotes() {
        Note note1 = new Note("Dr James", 1L, "report", LocalDateTime.now());
        Note note2 = new Note("Dr Karl", 12L, "report report", LocalDateTime.now());
        Note note3 = new Note("Dr Jones", 34L, "report report report", LocalDateTime.now());
        when(noteRepositoryMock.findAll()).thenReturn(Arrays.asList(note1, note2, note3));

        List<Note> result = noteServiceUT.getAllNote();
        assertThat(result).hasSize(3).contains(note1, note2, note3);
    }

    @Test
    void getId_whenGetNoteById_thenReturnNote() {
        String id = "60f532903ded77001064ae92";
        Note note = new Note("Dr Jones", 34L, "report report report", LocalDateTime.now());
        note.setId(id);
        when(noteRepositoryMock.findById(id)).thenReturn(Optional.of(note));
        Note result = noteServiceUT.getNoteById(id);
        assertThat(result).isEqualTo(note);
    }

    @Test
    void getPatientId_whenGetAllNoteOfPatientId_thenReturnNotesOfPatient() {
        Note note1 = new Note("Dr James", 34L, "report", LocalDateTime.now());
        Note note2 = new Note("Dr Karl", 34L, "report report", LocalDateTime.now());
        Note note3 = new Note("Dr Jones", 34L, "report report report", LocalDateTime.now());
        when(noteRepositoryMock.findNoteByPatientId(34L))
                .thenReturn(Arrays.asList(note1, note2, note3));

        List<Note> result = noteServiceUT.getAllNoteOfPatientId(34L);
        assertThat(result).hasSize(3).contains(note1, note2, note3);
    }

    @Test
    void getNote_whenCreateNote_thenReturnNoteCreated() {
        Note note = new Note("Dr Jones", 34L, "report report report", LocalDateTime.now());
        String id = "60f532903ded77001064ae92";
        note.setId(id);
        when(noteRepositoryMock.save(note)).thenReturn(note);

        Note result = noteServiceUT.createNote(note);
        assertThat(result).isEqualTo(note);
    }

    @Test
    void getIdAndReport_whenUpdateReport_thenReturnNoteUpdated() {
        String reportUpdated = "test";
        Note note = new Note("Dr Jones", 34L, "report report report", LocalDateTime.now());
        String id = "60f532903ded77001064ae92";
        note.setId(id);
        when(noteRepositoryMock.findById(id)).thenReturn(Optional.of(note));
        when(noteRepositoryMock.save(note)).thenReturn(note);

        Note result = noteServiceUT.updateReport(id, reportUpdated);
        assertThat(result.getReport()).isEqualTo(reportUpdated);
    }
}
