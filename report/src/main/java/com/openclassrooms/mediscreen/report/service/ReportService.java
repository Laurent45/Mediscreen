package com.openclassrooms.mediscreen.report.service;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.enumeration.Gender;
import com.openclassrooms.mediscreen.report.enumeration.Level;
import com.openclassrooms.mediscreen.report.model.Note;
import com.openclassrooms.mediscreen.report.model.Patient;
import com.openclassrooms.mediscreen.report.proxy.NoteProxy;
import com.openclassrooms.mediscreen.report.proxy.PatientProxy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PatientProxy patientProxy;
    private final NoteProxy noteProxy;

    private static final List<String> TRIGGER_TERMS =
            Arrays.asList(
                    "hémoglobine a1c",
                    "microalbumine",
                    "taille",
                    "poids",
                    "fumeur",
                    "anormal",
                    "cholestérol",
                    "vertige",
                    "rechute",
                    "réaction",
                    "anticorps");

    // Risk assessment thresholds
    private static final int AGE_THRESHOLD = 30;
    private static final int BORDERLINE_TRIGGER_COUNT = 2;
    private static final int DANGER_TRIGGER_MIN = 3;
    private static final int DANGER_TRIGGER_MAX_UNDER_30 = 6;
    private static final int MALE_DANGER_TRIGGER_COUNT = 3;
    private static final int FEMALE_DANGER_TRIGGER_COUNT = 4;

    public ReportDTO getReportOfRisk(Long patientId) {
        Patient patient = patientProxy.getPatientById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found for id: " + patientId);
        }

        List<Note> notes = noteProxy.getNotesByPatientId(patientId);
        notes = notes != null ? notes : emptyList();

        int numberOfTrigger = countTriggers(notes);
        Level level = getLevelOfRisk(numberOfTrigger, patient);

        return new ReportDTO(
                patient.firstName(), patient.lastName(), patient.getAge(), level.getLevel());
    }

    private Level getLevelOfRisk(int trigger, Patient patient) {
        int patientAge = patient.getAge();
        Gender gender = patient.gender();

        if (isInDanger(gender, patientAge, trigger)) {
            return Level.IN_DANGER;
        }
        if (isEarlyOnset(gender, patientAge, trigger)) {
            return Level.EARLY_ONSET;
        }
        if (isBorderline(trigger)) {
            return Level.BORDERLINE;
        }
        return Level.NONE;
    }

    private boolean isInDanger(Gender gender, int age, int triggerCount) {
        if (isAdultWithDangerRangeTriggers(age, triggerCount)) {
            return true;
        }
        return hasGenderSpecificDangerThreshold(gender, triggerCount);
    }

    private boolean isEarlyOnset(Gender gender, int age, int triggerCount) {
        if (isAdultWithEarlyOnsetTriggers(age, triggerCount)) {
            return true;
        }
        return hasGenderSpecificEarlyOnsetThreshold(gender, triggerCount);
    }

    private boolean isBorderline(int triggerCount) {
        return triggerCount == BORDERLINE_TRIGGER_COUNT;
    }

    private int countTriggers(List<Note> notes) {
        return (int)
                notes.stream()
                        .map(Note::report)
                        .flatMap(this::extractTriggerTerms)
                        .distinct()
                        .count();
    }

    private Stream<String> extractTriggerTerms(String report) {
        if (report == null || report.isEmpty()) {
            return Stream.empty();
        }
        String lowerReport = report.toLowerCase();
        return TRIGGER_TERMS.stream().filter(lowerReport::contains);
    }

    private boolean isAdultWithDangerRangeTriggers(int age, int triggerCount) {
        return age > AGE_THRESHOLD
                && triggerCount > DANGER_TRIGGER_MIN
                && triggerCount <= DANGER_TRIGGER_MAX_UNDER_30;
    }

    private boolean isAdultWithEarlyOnsetTriggers(int age, int triggerCount) {
        return age > AGE_THRESHOLD && triggerCount > DANGER_TRIGGER_MAX_UNDER_30;
    }

    private boolean hasGenderSpecificDangerThreshold(Gender gender, int triggerCount) {
        return (gender == Gender.MALE && triggerCount == MALE_DANGER_TRIGGER_COUNT)
                || (gender == Gender.FEMALE && triggerCount == FEMALE_DANGER_TRIGGER_COUNT);
    }

    private boolean hasGenderSpecificEarlyOnsetThreshold(Gender gender, int triggerCount) {
        return (gender == Gender.MALE && triggerCount > MALE_DANGER_TRIGGER_COUNT)
                || (gender == Gender.FEMALE && triggerCount > FEMALE_DANGER_TRIGGER_COUNT);
    }
}
