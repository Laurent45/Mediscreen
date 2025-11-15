package com.openclassrooms.mediscreen.report.service;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.enumeration.Level;
import com.openclassrooms.mediscreen.report.model.Note;
import com.openclassrooms.mediscreen.report.model.Patient;
import com.openclassrooms.mediscreen.report.proxy.NoteProxy;
import com.openclassrooms.mediscreen.report.proxy.PatientProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PatientProxy patientProxy;
    private final NoteProxy noteProxy;

    private static final List<String> TRIGGER_TERMS = Arrays.asList(
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
    );

    public ReportDTO getReportOfRisk(Long patientId) {
        Patient patient = patientProxy.getPatientById(patientId);
        List<Note> notes = noteProxy.getNotesByPatientId(patientId);
        int numberOfTrigger = getNumberOfTrigger(notes);
        Level level = getLevelOfRisk(numberOfTrigger, patient);

        return new ReportDTO(patient.getFirstName(),
                patient.getLastName(),
                LocalDate.now().getYear() - patient.getDateOfBirth().getYear(),
                level.getLevel());
    }

    public Level getLevelOfRisk(int trigger, Patient patient) {
        int agePatient = LocalDate.now().getYear() - patient.getDateOfBirth().getYear();
        String gender = patient.getGender();
        if (agePatient <= 30) {
            if ((gender.equals("M") && trigger == 3) || (gender.equals("F") && trigger == 4)) {
                return Level.IN_DANGER;
            }
            if ((gender.equals("M") && trigger > 3) || (gender.equals("F") && trigger > 4)) {
                return Level.EARLY_ONSET;
            }
        }
        else {
            if (trigger == 2) {
                return Level.BORDERLINE;
            }
            if (trigger > 2 && trigger <= 6) {
                return Level.IN_DANGER;
            }
            if (trigger > 6) {
                return Level.EARLY_ONSET;
            }
        }
        return Level.NONE;
    }

    private int getNumberOfTrigger(List<Note> notes) {
        Set<String> triggers = new HashSet<>();

        for (Note note : notes) {
            triggers.addAll(TRIGGER_TERMS.stream()
                    .filter(terms -> note.getReport().toLowerCase().contains(terms.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        return triggers.size();
    }
}
