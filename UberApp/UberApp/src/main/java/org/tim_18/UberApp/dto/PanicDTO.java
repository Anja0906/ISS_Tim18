package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Panic;

@Data
public class PanicDTO {
    private String reason;
    public PanicDTO(){}

    public PanicDTO(String reason) {
        this.reason = reason;
    }

    public PanicDTO(Panic panic){
        this(panic.getReason());
    }
}
