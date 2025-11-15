package com.openclassrooms.mediscreen.frontend.controller;

import com.openclassrooms.mediscreen.frontend.dto.NoteDTO;
import com.openclassrooms.mediscreen.frontend.dto.ReportDTO;
import com.openclassrooms.mediscreen.frontend.model.Note;
import com.openclassrooms.mediscreen.frontend.model.Patient;
import com.openclassrooms.mediscreen.frontend.proxy.NoteProxy;
import com.openclassrooms.mediscreen.frontend.proxy.PatientProxy;
import com.openclassrooms.mediscreen.frontend.proxy.ReportProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("ui/note")
@RequiredArgsConstructor
public class NoteController {

    private final PatientProxy patientProxy;
    private final NoteProxy noteProxy;
    private final ReportProxy reportProxy;

    @GetMapping("/{id}")
    public String getNoteByPatientId(@PathVariable("id") Long patientId
            , Model model) {
        Patient patient = patientProxy.getPatientById(patientId);
        model.addAttribute("patient", patient);
        List<Note> notes = noteProxy.getNotesByPatientId(patientId);
        model.addAttribute("notes", notes);
        ReportDTO report = reportProxy.getReport(patientId);
        model.addAttribute("report", report);
        model.addAttribute("noteDTO", new NoteDTO());
        return "note/index";
    }

    @PostMapping("/createNote/{id}")
    public String createNote(@PathVariable("id") Long patientId
            , NoteDTO noteDTO
            , RedirectAttributes redirectAttributes) {
        Note note = new Note(noteDTO.getPractitionerName(),
                patientId,
                noteDTO.getReport(),
                LocalDateTime.now());
        noteProxy.create(note);
        String message =
                "Created note by " + noteDTO.getPractitionerName() + " at " + note.getCreated() + " ✨.";
        redirectAttributes.addFlashAttribute("noteCreated", message);
        return "redirect:/ui/note/" + patientId;
    }

    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable("id") String id
            , NoteDTO noteDTO
            , RedirectAttributes redirectAttributes) {
        Note note = noteProxy.updateReportById(id, noteDTO.getReport());
        String message = "<b> Updated note </b>";
        redirectAttributes.addFlashAttribute("noteUpdated", message);
        return "redirect:/ui/note/" + note.getPatientId();
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") String id
            , RedirectAttributes redirectAttributes) {
        Long patientId = noteProxy.getNoteById(id).getPatientId();
        noteProxy.deleteNoteById(id);
        String message = "<b> Deleted note </b>";
        redirectAttributes.addFlashAttribute("noteDeleted", message);
        return "redirect:/ui/note/" + patientId;
    }
}
