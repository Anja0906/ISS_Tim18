package org.tim_18.UberApp.dto.noteDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Note;

import java.time.LocalDateTime;

@Data
public class NoteResponseDTO {
    private Integer id;
    private LocalDateTime date;
    private String message;

    public NoteResponseDTO() {
    }

    public NoteResponseDTO(Integer id, LocalDateTime date, String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public NoteResponseDTO(Note note) {
        this.id = note.getId();
        this.date = LocalDateTime.now();
        this.message = note.getMessage();
    }

}
