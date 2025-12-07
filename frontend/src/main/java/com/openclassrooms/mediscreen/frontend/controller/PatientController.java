package com.openclassrooms.mediscreen.frontend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.mediscreen.frontend.model.Patient;
import com.openclassrooms.mediscreen.frontend.proxy.PatientProxy;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("ui/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientProxy patientProxy;

    private static final String REDIRECT_INDEX = "redirect:/ui/patient/";

    @GetMapping("/")
    public String getIndex(Model model) {
        List<Patient> patients = patientProxy.getPatients();
        model.addAttribute("patients", patients);
        return "patient/index";
    }

    @GetMapping("/create")
    public String getCreatePatient(Model model) {
        Patient patient = new Patient();
        model.addAttribute("patient", patient);
        return "patient/create";
    }

    @PostMapping("/create")
    public String createPatient(
            RedirectAttributes redirectAttributes, @Valid Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            return "patient/create";
        }
        patientProxy.savePatient(patient);
        String message =
                "Created user <b>"
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName()
                        + "</b> ✨.";
        redirectAttributes.addFlashAttribute("patientCreated", message);
        return REDIRECT_INDEX;
    }

    @GetMapping("/update/{id}")
    public String getPatient(Model model, @PathVariable("id") Long id) {
        Patient patient = patientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(
            RedirectAttributes redirectAttributes,
            @PathVariable("id") Long id,
            @Valid Patient patient,
            BindingResult result) {
        if (result.hasErrors()) {
            return "patient/edit";
        }
        patientProxy.updatePatient(id, patient);
        String message =
                "Updated  patient <b>"
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName()
                        + "</b>.";
        redirectAttributes.addFlashAttribute("patientUpdated", message);
        return REDIRECT_INDEX;
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(
            @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        patientProxy.deletePatient(id);
        String message = "Delete patient <b>" + id + "</b>.";
        redirectAttributes.addFlashAttribute("patientDeleted", message);
        return REDIRECT_INDEX;
    }
}
