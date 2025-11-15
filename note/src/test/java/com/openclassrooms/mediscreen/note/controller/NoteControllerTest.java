package com.openclassrooms.mediscreen.note.controller;

import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.service.INoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private INoteService noteServiceMock;
    @InjectMocks
    private NoteController noteControllerUT;

    @Test
    void whenGetNotes_thenReturnAllNotes() {
        List<Note> notes = Arrays.asList(
                new Note("Dr james"
                        , 1L
                        , "report"
                        , LocalDateTime.now()),
                new Note("Dr Mike"
                        , 2L
                        , "report report"
                        , LocalDateTime.now())
        );
        when(noteServiceMock.getAllNote()).thenReturn(notes);

        List<Note> result = noteControllerUT.getNotes();
        assertThat(result).containsAll(notes);
    }

    @Test
    void getId_whenGetNoteById_thenReturnNote() {
        Note note = new Note("Dr james"
                , 1L
                , "report"
                , LocalDateTime.now());
        when(noteServiceMock.getNoteById("507f1f77bcf86cd799439011")).thenReturn(note);

        Note result = noteControllerUT.getNoteById("507f1f77bcf86cd799439011");
        assertThat(result).isEqualTo(note);
    }

    @Test
    void getIdPatient_whenGetNotesByPatientId_thenReturnAllNotes() {
        List<Note> notes = Arrays.asList(
                new Note("Dr james"
                        , 1L
                        , "report"
                        , LocalDateTime.now()),
                new Note("Dr Mike"
                        , 1L
                        , "report report"
                        , LocalDateTime.now())
        );
        when(noteServiceMock.getAllNoteOfPatientId(1L)).thenReturn(notes);

        List<Note> result = noteControllerUT.getNotesByPatientId(1L);
        assertThat(result).containsAll(notes);
    }

    @Test
    void getNote_whenCreate_thenReturnNoteCreated() {
        Note note = new Note("Dr james"
                , 1L
                , "report"
                , LocalDateTime.now());
        when(noteServiceMock.createNote(note)).thenReturn(note);

        Note result = noteControllerUT.create(note);
        assertThat(result).isEqualTo(note);
    }
}