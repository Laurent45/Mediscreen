package com.openclassrooms.mediscreen.report.proxy;

import com.openclassrooms.mediscreen.report.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Validated
@FeignClient(name = "patientApi", url = "http://patient:8081/api/v1/patient/")
public interface PatientProxy {

    @GetMapping("/list")
    List<Patient> getPatients();

    @GetMapping("/{id}")
    Patient getPatientById(@PathVariable("id") Long id);

}