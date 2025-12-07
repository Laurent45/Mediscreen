package com.openclassrooms.mediscreen.patient.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.validation.GenderConstraint;

public record PatientRequestDto(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,
        @GenderConstraint(message = "Gender must be M (Male) or F (Female)") String gender,
        String address,
        String phone) {

    public Patient toEntity() {
        return new Patient(null, firstName, lastName, dateOfBirth, gender, address, phone);
    }

    public Patient toEntity(Long id) {
        return new Patient(id, firstName, lastName, dateOfBirth, gender, address, phone);
    }
}
