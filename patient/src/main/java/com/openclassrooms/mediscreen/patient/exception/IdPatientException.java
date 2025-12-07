package com.openclassrooms.mediscreen.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Log4j2
public class IdPatientException extends RuntimeException {
    public IdPatientException(String message) {
        super(message);
        log.info(message);
    }
}
