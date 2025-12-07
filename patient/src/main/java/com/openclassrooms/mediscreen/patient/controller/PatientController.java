package com.openclassrooms.mediscreen.patient.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mediscreen.patient.controller.dto.PatientRequestDto;
import com.openclassrooms.mediscreen.patient.controller.dto.PatientResponseDto;
import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "CRUD operations for patient management")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Retrieve all patients")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "List of all patients returned")
            })
    @GetMapping
    public List<PatientResponseDto> getAllPatients() {
        return patientService.findAll().stream().map(PatientResponseDto::from).toList();
    }

    @Operation(summary = "Retrieve a patient by ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Patient found and returned"),
                @ApiResponse(responseCode = "404", description = "Patient not found")
            })
    @GetMapping("/{id}")
    public PatientResponseDto getPatientById(@PathVariable Long id) {
        return PatientResponseDto.from(patientService.findById(id));
    }

    @Operation(summary = "Create a new patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Patient created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request body")
            })
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @RequestBody @Valid PatientRequestDto patientRequest) {
        Patient savedPatient = patientService.create(patientRequest.toEntity());
        PatientResponseDto response = PatientResponseDto.from(savedPatient);
        return ResponseEntity.created(URI.create("/api/v1/patients/" + savedPatient.getId()))
                .body(response);
    }

    @Operation(summary = "Update an existing patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request body"),
                @ApiResponse(responseCode = "404", description = "Patient not found")
            })
    @PutMapping("/{id}")
    public PatientResponseDto updatePatient(
            @PathVariable Long id, @RequestBody @Valid PatientRequestDto patientRequest) {
        Patient updatedPatient = patientService.update(id, patientRequest.toEntity(id));
        return PatientResponseDto.from(updatedPatient);
    }

    @Operation(summary = "Delete a patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Patient not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
