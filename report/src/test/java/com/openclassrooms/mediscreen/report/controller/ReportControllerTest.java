package com.openclassrooms.mediscreen.report.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.openclassrooms.mediscreen.report.dto.ReportDTO;
import com.openclassrooms.mediscreen.report.service.ReportService;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    private static final String BASE_URL = "/api/v1/report";
    private static final Long PATIENT_ID = 1L;

    @Autowired private MockMvc mockMvc;
    private RestTestClient client;

    @MockitoBean private ReportService reportService;

    @BeforeEach
    void setUp() {
        client = RestTestClient.bindTo(mockMvc).build();
    }

    @Nested
    @DisplayName("GET /api/v1/report/{patientId}")
    class GetPatientRiskReportTests {

        @Test
        @DisplayName("Should return patient risk report when patient exists")
        void shouldReturnPatientRiskReportWhenPatientExists() {
            ReportDTO expectedReport = createReportDTO();
            when(reportService.getReportOfRisk(PATIENT_ID)).thenReturn(expectedReport);

            client.get()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ReportDTO.class)
                    .isEqualTo(expectedReport);
        }

        @Test
        @DisplayName("Should return 404 when patient not found")
        void shouldReturn404WhenPatientNotFound() {
            when(reportService.getReportOfRisk(PATIENT_ID))
                    .thenThrow(
                            new IllegalArgumentException(
                                    "Patient not found for id: " + PATIENT_ID));

            client.get()
                    .uri(BASE_URL + "/" + PATIENT_ID)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isNotFound();
        }
    }

    private static ReportDTO createReportDTO() {
        return new ReportDTO("John", "Doe", 45, "In danger");
    }
}
