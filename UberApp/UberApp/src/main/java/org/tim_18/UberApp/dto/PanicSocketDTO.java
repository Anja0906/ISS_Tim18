package org.tim_18.UberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.model.Panic;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicSocketDTO {
    private Integer id;
    private String user;
    private String time;
    private String reason;

    public PanicSocketDTO(Panic panic) {
        this(panic.getId(), panic.getUser().getEmail(),
                 panic.getTime().toString(),
                panic.getReason());
    }
}