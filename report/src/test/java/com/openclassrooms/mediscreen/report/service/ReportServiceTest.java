package com.openclassrooms.mediscreen.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.enumeration.Gender;
import com.openclassrooms.mediscreen.report.model.Note;
import com.openclassrooms.mediscreen.report.model.Patient;
import com.openclassrooms.mediscreen.report.proxy.NoteProxy;
import com.openclassrooms.mediscreen.report.proxy.PatientProxy;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    private static final Long PATIENT_ID = 1L;
    private static final String PRACTITIONER_NAME = "Dr dibon";
    private static final String PATIENT_ADDRESS = "new york";
    private static final String PATIENT_PHONE = "000-111-222";

    @Mock private PatientProxy patientProxyMock;
    @Mock private NoteProxy noteProxyMock;
    @InjectMocks private ReportService reportServiceUT;

    @Nested
    @DisplayName("getReportOfRisk")
    class GetReportOfRiskTests {

        @Test
        @DisplayName(
                "Should return report with correct risk level for male patient with 3 triggers")
        void shouldReturnReportDTOWithMalePatient() {
            // Use a birthdate that is consistently older than age 30
            LocalDate birthDate = LocalDate.now().minusYears(40);
            Patient patient = createPatient("john", "doe", birthDate, Gender.MALE);
            List<Note> notes =
                    Arrays.asList(
                            createNote("Tests de laboratoire indiquant une microalbumine élevée"),
                            createNote(
                                    "Le patient déclare avoir eu plusieurs épisodes de vertige depuis la dernière visite"),
                            createNote(
                                    "Les tests de laboratoire indiquent que les anticorps sont élevés"),
                            createNote(
                                    "Le patient déclare avoir des douleurs au cou occasionnellement"));
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("In danger");
            assertThat(result.getAge())
                    .isGreaterThan(30); // Age is calculated dynamically, should be ~40
            verify(patientProxyMock).getPatientById(PATIENT_ID);
            verify(noteProxyMock).getNotesByPatientId(PATIENT_ID);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when patient not found")
        void shouldThrowExceptionWhenPatientNotFound() {
            when(patientProxyMock.getPatientById(999L)).thenReturn(null);

            assertThatThrownBy(() -> reportServiceUT.getReportOfRisk(999L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Patient not found");
        }

        @Test
        @DisplayName("Should handle null notes list gracefully")
        void shouldHandleEmptyNotes() {
            Patient patient = createPatient("john", "doe", LocalDate.of(1980, 1, 25), Gender.MALE);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(null);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("None");
        }
    }

    @Nested
    @DisplayName("Risk Level Assessment for Male Patients")
    class MalePatientRiskLevelTests {

        @ParameterizedTest
        @CsvSource({"2, Borderline", "4, In danger", "9, Early onset", "0, None"})
        @DisplayName("Should return correct risk level for male patient over 30")
        void shouldReturnCorrectLevelForMaleOver30(int triggers, String expectedLevel) {
            Patient patient = createPatient("john", "doe", LocalDate.of(1980, 1, 25), Gender.MALE);
            List<Note> notes = createNotesWithTriggers(triggers);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo(expectedLevel);
        }

        @Test
        @DisplayName("Should return IN_DANGER when male under 30 with exactly 3 triggers")
        void shouldReturnInDangerWhenUnder30WithThreeTriggers() {
            Patient patient = createPatient("john", "doe", LocalDate.of(2000, 1, 25), Gender.MALE);
            List<Note> notes = createNotesWithTriggers(3);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("In danger");
        }
    }

    @Nested
    @DisplayName("Risk Level Assessment for Female Patients")
    class FemalePatientRiskLevelTests {

        @ParameterizedTest
        @CsvSource({"2, Borderline", "4, In danger", "5, In danger", "9, Early onset", "0, None"})
        @DisplayName("Should return correct risk level for female patient over 30")
        void shouldReturnCorrectLevelForFemaleOver30(int triggers, String expectedLevel) {
            Patient patient =
                    createPatient("jane", "doe", LocalDate.of(1980, 1, 25), Gender.FEMALE);
            List<Note> notes = createNotesWithTriggers(triggers);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo(expectedLevel);
        }

        @Test
        @DisplayName("Should return IN_DANGER when female under 30 with exactly 4 triggers")
        void shouldReturnInDangerWhenUnder30WithFourTriggers() {
            Patient patient =
                    createPatient("jane", "doe", LocalDate.of(2000, 1, 25), Gender.FEMALE);
            List<Note> notes = createNotesWithTriggers(4);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("In danger");
        }
    }

    @Nested
    @DisplayName("Age Boundary Conditions")
    class AgeBoundaryTests {

        @Test
        @DisplayName("Should apply gender-specific rule when patient is exactly 30 years old")
        void shouldApplyGenderSpecificRuleAtAge30() {
            LocalDate birthDate = LocalDate.now().minusYears(30);
            Patient patient = createPatient("john", "doe", birthDate, Gender.MALE);
            List<Note> notes = createNotesWithTriggers(4);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("Early onset");
        }
    }

    @Nested
    @DisplayName("Trigger Detection and Counting")
    class TriggerDetectionTests {

        @Test
        @DisplayName(
                "Should count only unique triggers even when same trigger appears multiple times")
        void shouldCountUniqueTriggers() {
            List<Note> notes =
                    Arrays.asList(
                            createNote("Patient has hémoglobine A1C and cholestérol issues"),
                            createNote("Patient shows vertige symptoms"),
                            createNote("hémoglobine A1C mentioned again"));
            Patient patient = createPatient("john", "doe", LocalDate.of(1980, 1, 25), Gender.MALE);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("In danger");
        }

        @Test
        @DisplayName("Should handle null reports without throwing exception")
        void shouldHandleNullReport() {
            List<Note> notes = new ArrayList<>();
            notes.add(new Note(null, "Dr", PATIENT_ID, null, LocalDateTime.now()));
            Patient patient = createPatient("john", "doe", LocalDate.of(1980, 1, 25), Gender.MALE);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("None");
        }

        @Test
        @DisplayName("Should detect triggers case-insensitively")
        void shouldBeCaseInsensitive() {
            List<Note> notes =
                    Arrays.asList(
                            createNote("Tests show HÉMOGLOBINE A1C"),
                            createNote("Patient has CHOLESTÉROL issues"));
            Patient patient = createPatient("john", "doe", LocalDate.of(1980, 1, 25), Gender.MALE);
            when(patientProxyMock.getPatientById(PATIENT_ID)).thenReturn(patient);
            when(noteProxyMock.getNotesByPatientId(PATIENT_ID)).thenReturn(notes);

            ReportDTO result = reportServiceUT.getReportOfRisk(PATIENT_ID);

            assertThat(result.getLevel()).isEqualTo("Borderline");
        }
    }

    private static Patient createPatient(
            String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
        return new Patient(
                PATIENT_ID,
                firstName,
                lastName,
                dateOfBirth,
                gender,
                PATIENT_ADDRESS,
                PATIENT_PHONE);
    }

    private static Note createNote(String report) {
        return new Note(null, PRACTITIONER_NAME, PATIENT_ID, report, LocalDateTime.now());
    }

    private static List<Note> createNotesWithTriggers(int triggerCount) {
        String[] triggers = {
            "hémoglobine A1C",
            "microalbumine",
            "taille",
            "poids",
            "fumeur",
            "anormal",
            "cholestérol",
            "vertige",
            "rechute",
            "réaction",
            "anticorps"
        };
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < triggerCount && i < triggers.length; i++) {
            notes.add(createNote("Patient has " + triggers[i]));
        }
        return notes;
    }
}
