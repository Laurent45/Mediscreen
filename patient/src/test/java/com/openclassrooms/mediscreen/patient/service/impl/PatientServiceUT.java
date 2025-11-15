package com.openclassrooms.mediscreen.patient.service.impl;

import com.openclassrooms.mediscreen.patient.enumeration.Gender;
import com.openclassrooms.mediscreen.patient.exception.IdPatientException;
import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceUT {

    @Mock
    private PatientRepository patientRepositoryMock;

    @InjectMocks
    private PatientService patientServiceUT;

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenGetAllPatients_thenReturnList() {
        List<Patient> patients = Arrays.asList(
                new Patient(1L
                        , "lolo"
                        , "zigoto"
                        , LocalDate.now().minusYears(12L)
                        , Gender.MALE.getSymbol()
                        , "address1"
                        , "000"),
                new Patient(2L
                        , "fafa"
                        , "zigota"
                        , LocalDate.now().minusYears(29L)
                        , Gender.FEMALE.getSymbol()
                        , "address2"
                        , "111"),
                new Patient(3L
                        , "dede"
                        , "pizza"
                        , LocalDate.now().minusDays(45L)
                        , Gender.MALE.getSymbol()
                        , "address3"
                        , "222")
        );
        when(patientRepositoryMock.findAll()).thenReturn(patients);

        List<Patient> result = patientServiceUT.getAllPatients();
        assertThat(result).hasSize(3);
        assertTrue(result.containsAll(patients));
    }

    @Test
    void getId_whenGetPatient_thenReturnPatient() {
        Patient patient = new Patient(1L
                , "lolo"
                , "zigoto"
                , LocalDate.now().minusYears(12L)
                , Gender.MALE.getSymbol()
                , "address1"
                , "000");
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientServiceUT.getPatient(1L);
        assertThat(result).isEqualTo(patient);
    }

    @Test
    void getWrongId_whenGetPatient_thenThrowException() {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> patientServiceUT.getPatient(1L)).isInstanceOf(IdPatientException.class);
    }

    @Test
    void getPatient_whenSavePatient_thenReturnPatientSaved() {
        Patient patient = new Patient(1L
                , "lolo"
                , "zigoto"
                , LocalDate.now().minusYears(12L)
                , Gender.MALE.getSymbol()
                , "address1"
                , "000");
        when(patientRepositoryMock.save(patient)).thenReturn(patient);

        Patient result = patientServiceUT.savePatient(patient);
        assertThat(result).isEqualTo(patient);
    }

    @Test
    void getIdAndPatient_whenUpdatePatient_thenReturnPatientUpdated() {
        Patient patient = new Patient(1L
                , "lolo"
                , "zigoto"
                , LocalDate.now().minusYears(12L)
                , Gender.MALE.getSymbol()
                , "address1"
                , "000");
        Patient patient1 = new Patient(null
                , "robin"
                , "batman"
                , LocalDate.of(1992, 12, 12)
                , Gender.FEMALE.getSymbol()
                , "address updated"
                , "999-999");
        Patient patientUpdated = new Patient(1L
                , "robin"
                , "batman"
                , LocalDate.of(1992, 12, 12)
                , Gender.FEMALE.getSymbol()
                , "address updated"
                , "999-999");
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepositoryMock.save(patientUpdated)).thenReturn(patientUpdated);

        Patient result = patientServiceUT.updatePatient(1L, patient1);
        assertThat(result).isEqualTo(patientUpdated);
    }

    @Test
    void getId_whenDeletePatient_thenReturnNothing() {
        Patient patient = new Patient(1L
                , "lolo"
                , "batman"
                , LocalDate.of(1992, 12, 12)
                , Gender.FEMALE.getSymbol()
                , "address1"
                , "000");
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));

        patientServiceUT.deletePatient(1L);
        verify(patientRepositoryMock, times(1)).delete(patient);
    }
}
