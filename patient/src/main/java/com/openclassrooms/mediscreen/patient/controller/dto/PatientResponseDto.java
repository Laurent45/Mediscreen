package com.openclassrooms.mediscreen.patient.controller.dto;

import java.time.LocalDate;

import com.openclassrooms.mediscreen.patient.model.Patient;

public record PatientResponseDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String phone) {

    public static PatientResponseDto from(Patient patient) {
        return new PatientResponseDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getAddress(),
                patient.getPhone());
    }
}
