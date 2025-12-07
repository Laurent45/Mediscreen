package com.openclassrooms.mediscreen.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.enumeration.Level;
import com.openclassrooms.mediscreen.report.model.Note;
import com.openclassrooms.mediscreen.report.model.Patient;
import com.openclassrooms.mediscreen.report.proxy.NoteProxy;
import com.openclassrooms.mediscreen.report.proxy.PatientProxy;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private PatientProxy patientProxyMock;
    @Mock private NoteProxy noteProxyMock;
    @InjectMocks private ReportService reportServiceUT;

    @Test
    void getPatientId_whenGetReport_thenReturnReportDTO() {
        Patient patient =
                new Patient(
                        1L,
                        "john",
                        "doe",
                        LocalDate.of(1992, 1, 25),
                        "M",
                        "new york",
                        "000-111-222");
        Note note =
                new Note(
                        "Dr dibon",
                        1L,
                        "Tests de laboratoire indiquant une microalbumine élevée",
                        LocalDateTime.now());
        Note note1 =
                new Note(
                        "Dr dibon",
                        1L,
                        "Le patient déclare avoir eu plusieurs épisodes de vertige depuis la dernière visite",
                        LocalDateTime.now());
        Note note2 =
                new Note(
                        "Dr dibon",
                        1L,
                        "Les tests de laboratoire indiquent que les anticorps sont élevés",
                        LocalDateTime.now());
        Note note3 =
                new Note(
                        "Dr dibon",
                        1L,
                        "Le patient déclare avoir des douleurs au cou occasionnellement",
                        LocalDateTime.now());
        when(patientProxyMock.getPatientById(1L)).thenReturn(patient);
        when(noteProxyMock.getNotesByPatientId(1L))
                .thenReturn(Arrays.asList(note, note1, note2, note3));

        ReportDTO result = reportServiceUT.getReportOfRisk(1L);
        assertThat(result.getLevel()).isEqualTo("In danger");
        assertThat(result.getAge()).isEqualTo(30);
    }

    @Test
    void getPatientGreaterThan30OldAndTwoTriggers_thenReturnLevel() {
        Patient patient =
                new Patient(
                        1L,
                        "john",
                        "doe",
                        LocalDate.of(1980, 1, 25),
                        "M",
                        "new york",
                        "000-111-222");

        Level result = reportServiceUT.getLevelOfRisk(2, patient);
        assertThat(result.getLevel()).isEqualTo(Level.BORDERLINE.getLevel());
    }

    @Test
    void getPatientGreaterThan30OldAndFourTriggers_thenReturnLevel() {
        Patient patient =
                new Patient(
                        1L,
                        "john",
                        "doe",
                        LocalDate.of(1980, 1, 25),
                        "M",
                        "new york",
                        "000-111-222");

        Level result = reportServiceUT.getLevelOfRisk(4, patient);
        assertThat(result.getLevel()).isEqualTo(Level.IN_DANGER.getLevel());
    }

    @Test
    void getPatientGreaterThan30OldAndNineTriggers_thenReturnLevel() {
        Patient patient =
                new Patient(
                        1L,
                        "john",
                        "doe",
                        LocalDate.of(1980, 1, 25),
                        "M",
                        "new york",
                        "000-111-222");

        Level result = reportServiceUT.getLevelOfRisk(9, patient);
        assertThat(result.getLevel()).isEqualTo(Level.EARLY_ONSET.getLevel());
    }

    @Test
    void getPatientGreaterThan30OldAndNoneTriggers_thenReturnLevel() {
        Patient patient =
                new Patient(
                        1L,
                        "john",
                        "doe",
                        LocalDate.of(1980, 1, 25),
                        "M",
                        "new york",
                        "000-111-222");

        Level result = reportServiceUT.getLevelOfRisk(0, patient);
        assertThat(result.getLevel()).isEqualTo(Level.NONE.getLevel());
    }
}
