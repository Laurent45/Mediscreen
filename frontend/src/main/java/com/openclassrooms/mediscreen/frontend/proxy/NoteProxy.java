package com.openclassrooms.mediscreen.frontend.proxy;

import com.openclassrooms.mediscreen.frontend.model.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "noteApi", url = "http://note:8080/api/v1/notePatient/")
public interface NoteProxy {

    @GetMapping("/allNotes")
    List<Note> getNotes();

    @GetMapping("/{id}")
    Note getNoteById(@PathVariable("id") String id);

    @GetMapping("/notes/{patientId}")
    List<Note> getNotesByPatientId(@PathVariable("patientId") Long patientId);

    @PostMapping("/create")
    Note create(@RequestBody Note note);

    @PutMapping("/update/{id}")
    Note updateReportById(@PathVariable("id") String id,
                          @RequestParam("report") String report);

    @DeleteMapping("/remove/{id}")
    void deleteNoteById(@PathVariable("id") String id);
}
