package com.openclassrooms.mediscreen.frontend.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Patient {

    private Long id;
    private String firstName;
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "the date must be in the past")
    private LocalDate dateOfBirth;

    private String gender;
    private String address;
    private String phone;
}
