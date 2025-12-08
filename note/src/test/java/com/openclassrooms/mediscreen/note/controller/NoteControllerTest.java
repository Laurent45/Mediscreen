package com.openclassrooms.mediscreen.note.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.openclassrooms.mediscreen.note.controller.dto.NoteResponseDto;
import com.openclassrooms.mediscreen.note.exception.NoteNotFoundException;
import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.service.NoteService;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    private static final String BASE_URL = "/api/v1/notes";
    private static final String NOTE_ID = "60f532903ded77001064ae92";
    private static final Long PATIENT_ID = 1L;
    private static final String PRACTITIONER_NAME = "Dr. Smith";
    private static final String REPORT = "Patient shows normal vital signs";
    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2024, 1, 15, 10, 30);

    @Autowired private MockMvc mockMvc;
    private RestTestClient client;

    @MockitoBean private NoteService noteService;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindTo(mockMvc).build();
    }

    @Nested
    @DisplayName("GET /api/v1/notes")
    class GetAllNotesTests {

        @Test
        @DisplayName("Should return all notes")
        void shouldReturnAllNotes() {
            when(noteService.findAll()).thenReturn(createNoteList());

            client.get()
                    .uri(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<NoteResponseDto>>() {})
                    .isEqualTo(createExpectedResponseList());
        }

        @Test
        @DisplayName("Should return empty list when no notes exist")
        void shouldReturnEmptyListWhenNoNotesExist() {
            when(noteService.findAll()).thenReturn(List.of());

            client.get()
                    .uri(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<NoteResponseDto>>() {})
                    .isEqualTo(List.of());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/notes/{id}")
    class GetNoteByIdTests {

        @Test
        @DisplayName("Should return note when found")
        void shouldReturnNoteWhenFound() {
            Note note = createNote(NOTE_ID);
            when(noteService.findById(NOTE_ID)).thenReturn(note);

            client.get()
                    .uri(BASE_URL + "/" + NOTE_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(NoteResponseDto.class)
                    .isEqualTo(NoteResponseDto.from(note));
        }

        @Test
        @DisplayName("Should return 404 when note not found")
        void shouldReturn404WhenNoteNotFound() {
            when(noteService.findById(NOTE_ID)).thenThrow(new NoteNotFoundException(NOTE_ID));

            client.get()
                    .uri(BASE_URL + "/" + NOTE_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isNotFound();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/notes/patient/{patientId}")
    class GetNotesByPatientIdTests {

        @Test
        @DisplayName("Should return all notes for patient")
        void shouldReturnAllNotesForPatient() {
            List<Note> patientNotes = createNoteList();
            when(noteService.findByPatientId(PATIENT_ID)).thenReturn(patientNotes);

            client.get()
                    .uri(BASE_URL + "/patient/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<NoteResponseDto>>() {})
                    .isEqualTo(createExpectedResponseList());
        }

        @Test
        @DisplayName("Should return empty list when patient has no notes")
        void shouldReturnEmptyListWhenPatientHasNoNotes() {
            when(noteService.findByPatientId(PATIENT_ID)).thenReturn(List.of());

            client.get()
                    .uri(BASE_URL + "/patient/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<NoteResponseDto>>() {})
                    .isEqualTo(List.of());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/notes")
    class CreateNoteTests {

        @Test
        @DisplayName("Should create note and return 201")
        void shouldCreateNoteAndReturn201() {
            Note savedNote = createNote(NOTE_ID);
            when(noteService.create(any(Note.class))).thenReturn(savedNote);

            client.post()
                    .uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createNoteRequestJson())
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .expectHeader()
                    .location(BASE_URL + "/" + NOTE_ID)
                    .expectBody(NoteResponseDto.class)
                    .isEqualTo(NoteResponseDto.from(savedNote));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() {
            String invalidJson =
                    """
                    {
                        "practitionerName": "",
                        "patientId": null,
                        "report": ""
                    }
                    """;

            client.post()
                    .uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(invalidJson)
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/notes/{id}")
    class UpdateNoteReportTests {

        @Test
        @DisplayName("Should update note report and return updated data")
        void shouldUpdateNoteReportAndReturnUpdatedData() {
            Note updatedNote = createNote(NOTE_ID);
            updatedNote.setReport("Updated report content");
            when(noteService.updateReport(eq(NOTE_ID), any(String.class))).thenReturn(updatedNote);

            client.put()
                    .uri(BASE_URL + "/" + NOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createUpdateReportRequestJson())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(NoteResponseDto.class)
                    .isEqualTo(NoteResponseDto.from(updatedNote));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent note")
        void shouldReturn404WhenUpdatingNonExistentNote() {
            when(noteService.updateReport(eq(NOTE_ID), any(String.class)))
                    .thenThrow(new NoteNotFoundException(NOTE_ID));

            client.put()
                    .uri(BASE_URL + "/" + NOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createUpdateReportRequestJson())
                    .exchange()
                    .expectStatus()
                    .isNotFound();
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() {
            String invalidJson =
                    """
                    {
                        "report": ""
                    }
                    """;

            client.put()
                    .uri(BASE_URL + "/" + NOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(invalidJson)
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/notes/{id}")
    class DeleteNoteTests {

        @Test
        @DisplayName("Should delete note and return 204")
        void shouldDeleteNoteAndReturn204() {
            doNothing().when(noteService).delete(NOTE_ID);

            client.delete().uri(BASE_URL + "/" + NOTE_ID).exchange().expectStatus().isNoContent();

            verify(noteService).delete(NOTE_ID);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent note")
        void shouldReturn404WhenDeletingNonExistentNote() {
            doThrow(new NoteNotFoundException(NOTE_ID)).when(noteService).delete(NOTE_ID);

            client.delete().uri(BASE_URL + "/" + NOTE_ID).exchange().expectStatus().isNotFound();
        }
    }

    private static Note createNote(String id) {
        Note note = new Note(PRACTITIONER_NAME, PATIENT_ID, REPORT, CREATED_DATE);
        note.setId(id);
        return note;
    }

    private static List<Note> createNoteList() {
        return List.of(
                createNoteWithDetails("id1", "Dr. Smith", 1L, "Report 1", CREATED_DATE),
                createNoteWithDetails("id2", "Dr. Jones", 2L, "Report 2", CREATED_DATE.plusDays(1)),
                createNoteWithDetails(
                        "id3", "Dr. Brown", 3L, "Report 3", CREATED_DATE.plusDays(2)));
    }

    private static Note createNoteWithDetails(
            String id,
            String practitionerName,
            Long patientId,
            String report,
            LocalDateTime created) {
        Note note = new Note(practitionerName, patientId, report, created);
        note.setId(id);
        return note;
    }

    private static List<NoteResponseDto> createExpectedResponseList() {
        return createNoteList().stream().map(NoteResponseDto::from).toList();
    }

    private static String createNoteRequestJson() {
        return """
                {
                    "practitionerName": "%s",
                    "patientId": %d,
                    "report": "%s"
                }
                """
                .formatted(PRACTITIONER_NAME, PATIENT_ID, REPORT);
    }

    private static String createUpdateReportRequestJson() {
        return """
                {
                    "report": "Updated report content"
                }
                """;
    }
}
