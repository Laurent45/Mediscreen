package com.openclassrooms.mediscreen.note.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.mediscreen.note.exception.NoteNotFoundException;
import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.repository.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    private static final String NOTE_ID = "60f532903ded77001064ae92";
    private static final Long PATIENT_ID = 1L;
    private static final String PRACTITIONER_NAME = "Dr. Smith";
    private static final String REPORT = "Patient shows normal vital signs";

    @Mock private NoteRepository noteRepository;

    @InjectMocks private NoteService noteService;

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("Should return all notes")
        void shouldReturnAllNotes() {
            List<Note> expectedNotes = createNoteList();
            when(noteRepository.findAll()).thenReturn(expectedNotes);

            List<Note> result = noteService.findAll();

            assertThat(result).hasSize(3).containsExactlyElementsOf(expectedNotes);
            verify(noteRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no notes exist")
        void shouldReturnEmptyListWhenNoNotesExist() {
            when(noteRepository.findAll()).thenReturn(List.of());

            List<Note> result = noteService.findAll();

            assertThat(result).isEmpty();
            verify(noteRepository).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {

        @Test
        @DisplayName("Should return note when found")
        void shouldReturnNoteWhenFound() {
            Note expectedNote = createNote(NOTE_ID);
            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.of(expectedNote));

            Note result = noteService.findById(NOTE_ID);

            assertThat(result).isEqualTo(expectedNote);
            verify(noteRepository).findById(NOTE_ID);
        }

        @Test
        @DisplayName("Should throw NoteNotFoundException when not found")
        void shouldThrowExceptionWhenNotFound() {
            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> noteService.findById(NOTE_ID))
                    .isInstanceOf(NoteNotFoundException.class)
                    .hasMessageContaining(NOTE_ID);
        }
    }

    @Nested
    @DisplayName("findByPatientId")
    class FindByPatientIdTests {

        @Test
        @DisplayName("Should return all notes for patient")
        void shouldReturnAllNotesForPatient() {
            List<Note> expectedNotes = createNoteList();
            when(noteRepository.findNoteByPatientId(PATIENT_ID)).thenReturn(expectedNotes);

            List<Note> result = noteService.findByPatientId(PATIENT_ID);

            assertThat(result).hasSize(3).containsExactlyElementsOf(expectedNotes);
            verify(noteRepository).findNoteByPatientId(PATIENT_ID);
        }

        @Test
        @DisplayName("Should return empty list when patient has no notes")
        void shouldReturnEmptyListWhenPatientHasNoNotes() {
            when(noteRepository.findNoteByPatientId(PATIENT_ID)).thenReturn(List.of());

            List<Note> result = noteService.findByPatientId(PATIENT_ID);

            assertThat(result).isEmpty();
            verify(noteRepository).findNoteByPatientId(PATIENT_ID);
        }
    }

    @Nested
    @DisplayName("create")
    class CreateTests {

        @Test
        @DisplayName("Should create and return note with created timestamp")
        void shouldCreateAndReturnNoteWithTimestamp() {
            Note noteToCreate = createNote(null);
            noteToCreate.setCreated(null);
            Note savedNote = createNote(NOTE_ID);

            when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

            Note result = noteService.create(noteToCreate);

            assertThat(result).isEqualTo(savedNote);
            assertThat(result.getId()).isEqualTo(NOTE_ID);
            assertThat(noteToCreate.getCreated()).isNotNull();
            verify(noteRepository).save(noteToCreate);
        }
    }

    @Nested
    @DisplayName("updateReport")
    class UpdateReportTests {

        @Test
        @DisplayName("Should update and return note with new report")
        void shouldUpdateAndReturnNote() {
            String newReport = "Updated report content";
            Note existingNote = createNote(NOTE_ID);
            Note updatedNote = createNote(NOTE_ID);
            updatedNote.setReport(newReport);

            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.of(existingNote));
            when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

            Note result = noteService.updateReport(NOTE_ID, newReport);

            assertThat(result.getReport()).isEqualTo(newReport);
            verify(noteRepository).findById(NOTE_ID);
            verify(noteRepository).save(existingNote);
        }

        @Test
        @DisplayName("Should throw NoteNotFoundException when updating non-existent note")
        void shouldThrowExceptionWhenUpdatingNonExistentNote() {
            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> noteService.updateReport(NOTE_ID, "new report"))
                    .isInstanceOf(NoteNotFoundException.class)
                    .hasMessageContaining(NOTE_ID);
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {

        @Test
        @DisplayName("Should delete note when found")
        void shouldDeleteNoteWhenFound() {
            Note note = createNote(NOTE_ID);
            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.of(note));

            noteService.delete(NOTE_ID);

            verify(noteRepository).findById(NOTE_ID);
            verify(noteRepository).delete(note);
        }

        @Test
        @DisplayName("Should throw NoteNotFoundException when deleting non-existent note")
        void shouldThrowExceptionWhenDeletingNonExistentNote() {
            when(noteRepository.findById(NOTE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> noteService.delete(NOTE_ID))
                    .isInstanceOf(NoteNotFoundException.class)
                    .hasMessageContaining(NOTE_ID);
        }
    }

    private static Note createNote(String id) {
        Note note = new Note(PRACTITIONER_NAME, PATIENT_ID, REPORT, LocalDateTime.now());
        note.setId(id);
        return note;
    }

    private static List<Note> createNoteList() {
        return List.of(
                createNoteWithDetails("id1", "Dr. Smith", 1L, "Report 1"),
                createNoteWithDetails("id2", "Dr. Jones", 2L, "Report 2"),
                createNoteWithDetails("id3", "Dr. Brown", 3L, "Report 3"));
    }

    private static Note createNoteWithDetails(
            String id, String practitionerName, Long patientId, String report) {
        Note note = new Note(practitionerName, patientId, report, LocalDateTime.now());
        note.setId(id);
        return note;
    }
}
