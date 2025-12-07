package com.openclassrooms.mediscreen.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mediscreen.patient.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {}
