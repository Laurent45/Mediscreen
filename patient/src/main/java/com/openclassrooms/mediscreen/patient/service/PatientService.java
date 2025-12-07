package com.openclassrooms.mediscreen.patient.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mediscreen.patient.exception.PatientNotFoundException;
import com.openclassrooms.mediscreen.patient.model.Patient;
import com.openclassrooms.mediscreen.patient.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/** Service for managing patient operations. */
@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * Retrieves all patients.
     *
     * @return list of all patients
     */
    public List<Patient> findAll() {
        log.debug("Retrieving all patients");
        return patientRepository.findAll();
    }

    /**
     * Retrieves a patient by ID.
     *
     * @param id the patient ID
     * @return the patient
     * @throws PatientNotFoundException if patient not found
     */
    public Patient findById(Long id) {
        log.debug("Retrieving patient with id: {}", id);
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    /**
     * Creates a new patient.
     *
     * @param patient the patient to create
     * @return the created patient
     */
    @Transactional
    public Patient create(Patient patient) {
        log.debug("Creating patient: {}", patient);
        return patientRepository.save(patient);
    }

    /**
     * Updates an existing patient.
     *
     * @param id the patient ID
     * @param updatedPatient the patient data to update
     * @return the updated patient
     * @throws PatientNotFoundException if patient not found
     */
    @Transactional
    public Patient update(Long id, Patient updatedPatient) {
        log.debug("Updating patient with id: {}", id);
        Patient existingPatient = findById(id);

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setPhone(updatedPatient.getPhone());

        return existingPatient;
    }

    /**
     * Deletes a patient by ID.
     *
     * @param id the patient ID
     * @throws PatientNotFoundException if patient not found
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Deleting patient with id: {}", id);
        Patient patient = findById(id);
        patientRepository.delete(patient);
    }
}
