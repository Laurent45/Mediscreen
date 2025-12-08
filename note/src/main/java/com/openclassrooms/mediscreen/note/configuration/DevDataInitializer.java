package com.openclassrooms.mediscreen.note.configuration;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.openclassrooms.mediscreen.note.model.Note;
import com.openclassrooms.mediscreen.note.repository.NoteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Log4j2
public class DevDataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Override
    public void run(String... args) {
        if (noteRepository.count() == 0) {
            log.info("Loading dev seed data...");
            noteRepository.saveAll(createDevNotes());
            log.info("Dev seed data loaded successfully: {} notes created", noteRepository.count());
        } else {
            log.info("Dev seed data already exists, skipping initialization");
        }
    }

    private List<Note> createDevNotes() {
        LocalDateTime now = LocalDateTime.now();

        return List.of(
                // Patient 1 - Multiple visits
                new Note(
                        "Dr. Smith",
                        1L,
                        "Patient states that they are feeling well. Weight is within normal range.",
                        now.minusDays(30)),
                new Note(
                        "Dr. Smith",
                        1L,
                        "Patient reports occasional dizziness. Blood pressure measured at 130/85.",
                        now.minusDays(15)),
                new Note(
                        "Dr. Jones",
                        1L,
                        "Follow-up visit. Dizziness has subsided. Recommended continued monitoring.",
                        now.minusDays(5)),

                // Patient 2 - Diabetic risk indicators
                new Note(
                        "Dr. Brown",
                        2L,
                        "Patient shows signs of abnormal hemoglobin levels. Cholesterol slightly elevated.",
                        now.minusDays(60)),
                new Note(
                        "Dr. Brown",
                        2L,
                        "Lab results indicate elevated glucose levels. Recommending dietary changes.",
                        now.minusDays(20)),

                // Patient 3 - Healthy patient
                new Note(
                        "Dr. Smith",
                        3L,
                        "Annual checkup complete. All vitals normal. Patient in excellent health.",
                        now.minusDays(90)),

                // Patient 4 - Multiple risk factors
                new Note(
                        "Dr. Jones",
                        4L,
                        "Patient is a smoker. Reports feeling fatigued. Body weight above normal range.",
                        now.minusDays(45)),
                new Note(
                        "Dr. Jones",
                        4L,
                        "Blood pressure reading shows hypertension. Prescribed medication.",
                        now.minusDays(30)),
                new Note(
                        "Dr. Brown",
                        4L,
                        "Patient experiencing vertigo and showing signs of reaction to medication. Adjusting dosage.",
                        now.minusDays(10)));
    }
}
