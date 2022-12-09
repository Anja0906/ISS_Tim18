package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Rejection;

@Data
public class RejectionDTO {
    private String reason;
    private String time;

    public RejectionDTO(){}

    public RejectionDTO(Integer id, String reason, String time) {
        this.reason = reason;
        this.time = time;
    }

    public RejectionDTO(Rejection rejection){
        this(rejection.getId(), rejection.getReason(), rejection.getTime().toString());
    }
}
