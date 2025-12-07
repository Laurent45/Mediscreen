package com.openclassrooms.mediscreen.frontend.proxy;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mediscreen.frontend.model.Patient;

@Validated
@FeignClient(name = "patientApi", url = "http://patient:8081/api/v1/patient/")
public interface PatientProxy {

    @GetMapping("/list")
    List<Patient> getPatients();

    @GetMapping("/{id}")
    Patient getPatientById(@PathVariable("id") Long id);

    @PostMapping("/save")
    ResponseEntity<String> savePatient(@RequestBody @Valid Patient patient);

    @PutMapping("/update/{id}")
    Patient updatePatient(@PathVariable("id") Long id, @RequestBody @Valid Patient patient);

    @DeleteMapping("/delete/{id}")
    void deletePatient(@PathVariable("id") Long id);
}
