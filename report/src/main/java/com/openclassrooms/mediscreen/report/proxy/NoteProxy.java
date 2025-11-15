package com.openclassrooms.mediscreen.report.proxy;

import com.openclassrooms.mediscreen.report.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "noteApi", url = "http://note:8080/api/v1" +
        "/notePatient/")
public interface NoteProxy {

    @GetMapping("/allNotes")
    List<Note> getNotes();

    @GetMapping("/{id}")
    Note getNoteById(@PathVariable("id") String id);

    @GetMapping("/notes/{patientId}")
    List<Note> getNotesByPatientId(@PathVariable("patientId") Long patientId);
}
