package com.openclassrooms.mediscreen.patient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.mediscreen.patient.enumeration.Gender;
import com.openclassrooms.mediscreen.patient.exception.PatientNotFoundException;
import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    private static final Long PATIENT_ID = 1L;
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);

    @Mock private PatientRepository patientRepository;

    @InjectMocks private PatientService patientService;

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("Should return all patients")
        void shouldReturnAllPatients() {
            List<Patient> expectedPatients = createPatientList();
            when(patientRepository.findAll()).thenReturn(expectedPatients);

            List<Patient> result = patientService.findAll();

            assertThat(result).hasSize(3).containsExactlyElementsOf(expectedPatients);
            verify(patientRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no patients exist")
        void shouldReturnEmptyListWhenNoPatientsExist() {
            when(patientRepository.findAll()).thenReturn(List.of());

            List<Patient> result = patientService.findAll();

            assertThat(result).isEmpty();
            verify(patientRepository).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {

        @Test
        @DisplayName("Should return patient when found")
        void shouldReturnPatientWhenFound() {
            Patient expectedPatient = createPatient(PATIENT_ID);
            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(expectedPatient));

            Patient result = patientService.findById(PATIENT_ID);

            assertThat(result).isEqualTo(expectedPatient);
            verify(patientRepository).findById(PATIENT_ID);
        }

        @Test
        @DisplayName("Should throw PatientNotFoundException when not found")
        void shouldThrowExceptionWhenNotFound() {
            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.findById(PATIENT_ID))
                    .isInstanceOf(PatientNotFoundException.class)
                    .hasMessageContaining(PATIENT_ID.toString());
        }
    }

    @Nested
    @DisplayName("create")
    class CreateTests {

        @Test
        @DisplayName("Should create and return patient")
        void shouldCreateAndReturnPatient() {
            Patient patientToCreate = createPatient(null);
            Patient savedPatient = createPatient(PATIENT_ID);
            when(patientRepository.save(patientToCreate)).thenReturn(savedPatient);

            Patient result = patientService.create(patientToCreate);

            assertThat(result).isEqualTo(savedPatient);
            assertThat(result.getId()).isEqualTo(PATIENT_ID);
            verify(patientRepository).save(patientToCreate);
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateTests {

        @Test
        @DisplayName("Should update and return patient")
        void shouldUpdateAndReturnPatient() {
            Patient existingPatient = createPatient(PATIENT_ID);
            Patient updatedData =
                    new Patient(
                            null,
                            "UpdatedFirstName",
                            "UpdatedLastName",
                            BIRTH_DATE.minusYears(5),
                            Gender.FEMALE.getSymbol(),
                            "Updated Address",
                            "999-9999");

            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(existingPatient));

            Patient result = patientService.update(PATIENT_ID, updatedData);

            assertThat(result.getFirstName()).isEqualTo("UpdatedFirstName");
            assertThat(result.getLastName()).isEqualTo("UpdatedLastName");
            assertThat(result.getDateOfBirth()).isEqualTo(BIRTH_DATE.minusYears(5));
            assertThat(result.getGender()).isEqualTo(Gender.FEMALE.getSymbol());
            assertThat(result.getAddress()).isEqualTo("Updated Address");
            assertThat(result.getPhone()).isEqualTo("999-9999");
            verify(patientRepository).findById(PATIENT_ID);
        }

        @Test
        @DisplayName("Should throw PatientNotFoundException when updating non-existent patient")
        void shouldThrowExceptionWhenUpdatingNonExistentPatient() {
            Patient updatedData = createPatient(null);
            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.update(PATIENT_ID, updatedData))
                    .isInstanceOf(PatientNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {

        @Test
        @DisplayName("Should delete patient when found")
        void shouldDeletePatientWhenFound() {
            Patient patient = createPatient(PATIENT_ID);
            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));

            patientService.delete(PATIENT_ID);

            verify(patientRepository).findById(PATIENT_ID);
            verify(patientRepository).delete(patient);
        }

        @Test
        @DisplayName("Should throw PatientNotFoundException when deleting non-existent patient")
        void shouldThrowExceptionWhenDeletingNonExistentPatient() {
            when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> patientService.delete(PATIENT_ID))
                    .isInstanceOf(PatientNotFoundException.class);
        }
    }

    private static Patient createPatient(Long id) {
        return new Patient(
                id,
                "John",
                "Doe",
                BIRTH_DATE,
                Gender.MALE.getSymbol(),
                "123 Main Street",
                "555-1234");
    }

    private static List<Patient> createPatientList() {
        return List.of(
                new Patient(
                        1L,
                        "John",
                        "Doe",
                        BIRTH_DATE,
                        Gender.MALE.getSymbol(),
                        "123 Main Street",
                        "555-1234"),
                new Patient(
                        2L,
                        "Jane",
                        "Smith",
                        BIRTH_DATE.minusYears(5),
                        Gender.FEMALE.getSymbol(),
                        "456 Oak Avenue",
                        "555-5678"),
                new Patient(
                        3L,
                        "Bob",
                        "Johnson",
                        BIRTH_DATE.plusYears(10),
                        Gender.MALE.getSymbol(),
                        "789 Pine Road",
                        "555-9012"));
    }
}
