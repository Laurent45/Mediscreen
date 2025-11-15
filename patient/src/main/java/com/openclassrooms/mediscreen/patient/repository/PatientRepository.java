package com.openclassrooms.mediscreen.patient.repository;

import com.openclassrooms.mediscreen.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
