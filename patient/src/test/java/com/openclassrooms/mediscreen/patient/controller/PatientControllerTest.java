package com.openclassrooms.mediscreen.patient.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.openclassrooms.mediscreen.patient.controller.dto.PatientResponseDto;
import com.openclassrooms.mediscreen.patient.enumeration.Gender;
import com.openclassrooms.mediscreen.patient.exception.PatientNotFoundException;
import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.service.PatientService;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    private static final String BASE_URL = "/api/v1/patients";
    private static final Long PATIENT_ID = 1L;
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 15);

    @Autowired private MockMvc mockMvc;
    private RestTestClient client;

    @MockitoBean private PatientService patientService;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindTo(mockMvc).build();
    }

    @Nested
    @DisplayName("GET /api/v1/patients")
    class GetAllPatientsTests {

        @Test
        @DisplayName("Should return all patients")
        void shouldReturnAllPatients() {
            when(patientService.findAll()).thenReturn(createPatientList());

            client.get()
                    .uri(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<PatientResponseDto>>() {})
                    .isEqualTo(createExpectedResponseList());
        }

        @Test
        @DisplayName("Should return empty list when no patients exist")
        void shouldReturnEmptyListWhenNoPatientsExist() {
            when(patientService.findAll()).thenReturn(List.of());

            client.get()
                    .uri(BASE_URL)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<List<PatientResponseDto>>() {})
                    .isEqualTo(List.of());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/patients/{id}")
    class GetPatientByIdTests {

        @Test
        @DisplayName("Should return patient when found")
        void shouldReturnPatientWhenFound() {
            Patient patient = createPatient(PATIENT_ID);
            when(patientService.findById(PATIENT_ID)).thenReturn(patient);

            client.get()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(PatientResponseDto.class)
                    .isEqualTo(PatientResponseDto.from(patient));
        }

        @Test
        @DisplayName("Should return 404 when patient not found")
        void shouldReturn404WhenPatientNotFound() {
            when(patientService.findById(PATIENT_ID))
                    .thenThrow(new PatientNotFoundException(PATIENT_ID));

            client.get()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isNotFound();
        }
    }

    @Nested
    @DisplayName("POST /api/v1/patients")
    class CreatePatientTests {

        @Test
        @DisplayName("Should create patient and return 201")
        void shouldCreatePatientAndReturn201() {
            Patient savedPatient = createPatient(PATIENT_ID);
            when(patientService.create(any(Patient.class))).thenReturn(savedPatient);

            client.post()
                    .uri(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createPatientRequestJson())
                    .exchange()
                    .expectStatus()
                    .isCreated()
                    .expectHeader()
                    .location(BASE_URL + "/" + PATIENT_ID)
                    .expectBody(PatientResponseDto.class)
                    .isEqualTo(PatientResponseDto.from(savedPatient));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() {
            String invalidJson =
                    """
                    {
                        "firstName": "",
                        "lastName": "",
                        "dateOfBirth": "2050-01-01",
                        "gender": "X"
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
    @DisplayName("PUT /api/v1/patients/{id}")
    class UpdatePatientTests {

        @Test
        @DisplayName("Should update patient and return updated data")
        void shouldUpdatePatientAndReturnUpdatedData() {
            Patient updatedPatient = createPatient(PATIENT_ID);
            when(patientService.update(eq(PATIENT_ID), any(Patient.class)))
                    .thenReturn(updatedPatient);

            client.put()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createPatientRequestJson())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(PatientResponseDto.class)
                    .isEqualTo(PatientResponseDto.from(updatedPatient));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent patient")
        void shouldReturn404WhenUpdatingNonExistentPatient() {
            when(patientService.update(eq(PATIENT_ID), any(Patient.class)))
                    .thenThrow(new PatientNotFoundException(PATIENT_ID));

            client.put()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createPatientRequestJson())
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
                        "firstName": "",
                        "lastName": ""
                    }
                    """;

            client.put()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(invalidJson)
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/patients/{id}")
    class DeletePatientTests {

        @Test
        @DisplayName("Should delete patient and return 204")
        void shouldDeletePatientAndReturn204() {
            doNothing().when(patientService).delete(PATIENT_ID);

            client.delete()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .exchange()
                    .expectStatus()
                    .isNoContent();

            verify(patientService).delete(PATIENT_ID);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent patient")
        void shouldReturn404WhenDeletingNonExistentPatient() {
            doThrow(new PatientNotFoundException(PATIENT_ID))
                    .when(patientService)
                    .delete(PATIENT_ID);

            client.delete().uri(BASE_URL + "/" + PATIENT_ID).exchange().expectStatus().isNotFound();
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

    private static List<PatientResponseDto> createExpectedResponseList() {
        return createPatientList().stream().map(PatientResponseDto::from).toList();
    }

    private static String createPatientRequestJson() {
        return """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "dateOfBirth": "%s",
                    "gender": "M",
                    "address": "123 Main Street",
                    "phone": "555-1234"
                }
                """
                .formatted(BIRTH_DATE);
    }
}
