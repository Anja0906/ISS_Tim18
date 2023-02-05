package org.tim_18.UberApp.dto.noteDTOs;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class NotePostDTO {
    @Length(max = 500, message = "Max length of message is 500")
    private String message;

    public NotePostDTO() {}

    public NotePostDTO(String message) {
        this.message = message;
    }
}
