package org.tim_18.UberApp.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tim_18.UberApp.exception.DriverNotFoundException;
import org.tim_18.UberApp.exception.NoteNotFoundException;
import org.tim_18.UberApp.model.Driver;
import org.tim_18.UberApp.model.Note;
import org.tim_18.UberApp.repository.NoteRepository;

import java.util.HashSet;
import java.util.List;

@Service("noteService")
public class NoteService {
    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    public Page<Note> findNotesByUserId(Integer id, Pageable pageable) {
        return noteRepository.findByUserId(id, pageable);
    }


    public Note addNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> findAllDrivers() {
        return noteRepository.findAll();
    }

    public List<Note> findAll(){return noteRepository.findAll();
    }
    public void saveNote(Note note){noteRepository.save(note);}

    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }

    public Note findNoteById(Integer id) {
        return noteRepository.findNoteById(id)
                .orElseThrow(() -> new NoteNotFoundException("Driver by id " + id + " was not found"));
    }

    public void deletNote(Integer id) {
        noteRepository.deleteNoteById(id);
    }
}
