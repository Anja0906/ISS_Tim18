package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Rejection;

@Data
public class RejectionDTO {
    private String reason;
    private String timeOfRejection;

    public RejectionDTO(){}

    public RejectionDTO(String reason, String timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public RejectionDTO(Rejection rejection){
        this(rejection.getReason(),
             rejection.getTimeOfRejection().toString());
    }
}
