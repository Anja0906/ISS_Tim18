package org.tim_18.UberApp.dto.noteDTOs;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Note;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

@Data
public class NoteResponseDTO {
    private Integer id;
    private String date;
    private String message;

    public NoteResponseDTO() {}
    public NoteResponseDTO(Integer id, Date date, String message) {
        this.id         = id;
        this.date       = date.toString();
        this.message    = message;
    }

    public NoteResponseDTO(Note note) {
        this.id         = note.getId();
        this.date       = new Date().toString();
        this.message    = note.getMessage();
    }

    public HashSet<NoteResponseDTO> makeNoteResponseDTOS(Page<Note> notes){
        HashSet<NoteResponseDTO> noteResponseDTOS = new HashSet<>();
        for (Note note:notes) {
            noteResponseDTOS.add(new NoteResponseDTO(note));
        }
        return noteResponseDTOS;
    }

}
