package org.tim_18.UberApp.dto.noteDTOs;

import lombok.Data;

@Data
public class NotePostDTO {
    private String message;

    public NotePostDTO() {}

    public NotePostDTO(String message) {
        this.message = message;
    }
}
