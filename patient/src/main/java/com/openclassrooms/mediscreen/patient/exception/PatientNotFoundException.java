package com.openclassrooms.mediscreen.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Log4j2
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {
        super(String.format("Patient with id %d not found", id));
        log.info("Patient not found: {}", id);
    }

    public PatientNotFoundException(String message) {
        super(message);
        log.info(message);
    }
}
