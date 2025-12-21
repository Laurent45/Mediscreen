package com.openclassrooms.mediscreen.report.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;

import com.openclassrooms.mediscreen.report.enumeration.Gender;

public record Patient(
        Long id,
        String firstName,
        String lastName,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
        Gender gender,
        String address,
        String phone) {

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }
}
