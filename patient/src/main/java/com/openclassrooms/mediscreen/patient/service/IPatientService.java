package com.openclassrooms.mediscreen.patient.service;

import java.util.List;

import com.openclassrooms.mediscreen.patient.model.Patient;

public interface IPatientService {

    /**
     * Get all patient save in db
     *
     * @return A list of Patient
     */
    List<Patient> getAllPatients();

    /**
     * Get a patient by id.
     *
     * @param id an id's patient
     * @return A patient. Return null if id's patient doesn't exist exists in db
     */
    Patient getPatient(Long id);

    /**
     * Save the patient given in parameter.
     *
     * @param patient a patient to be saved
     * @return A patient saved
     */
    Patient savePatient(Patient patient);

    /**
     * Update information's patient.
     *
     * @param patient a patient with new information
     * @param id id's patient to update
     * @return A patient updated
     */
    Patient updatePatient(Long id, Patient patient);

    /**
     * Delete a patient by id.
     *
     * @param id id's patient
     */
    void deletePatient(Long id);
}
