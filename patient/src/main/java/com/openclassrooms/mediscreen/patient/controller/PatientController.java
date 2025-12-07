package com.openclassrooms.mediscreen.patient.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.service.IPatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "CRUD operations about patient information")
public class PatientController {

    private final IPatientService patientService;

    @Operation(summary = "Get all patient")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All patient return")})
    @GetMapping("/list")
    public List<Patient> getPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Get a patient by id")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Return a patient by id"),
                @ApiResponse(responseCode = "404", description = "Patient doesn't exist")
            })
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable("id") Long id) {
        return patientService.getPatient(id);
    }

    @Operation(summary = "Create a new patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "A patient created"),
                @ApiResponse(responseCode = "400", description = "Some fields are not valid")
            })
    @PostMapping("/save")
    public ResponseEntity<String> savePatient(@RequestBody @Valid Patient patient) {
        Patient patientSaved = patientService.savePatient(patient);
        return ResponseEntity.created(URI.create("/patient/" + patientSaved.getId())).build();
    }

    @Operation(summary = "Update a patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "A patient updated"),
                @ApiResponse(responseCode = "400", description = "Some fields are not valid"),
                @ApiResponse(responseCode = "404", description = "Patient doesn't exist")
            })
    @PutMapping("/update/{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @RequestBody @Valid Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @Operation(summary = "Delete a patient")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "A patient deleted"),
                @ApiResponse(responseCode = "404", description = "Patient doesn't exist")
            })
    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
    }
}
