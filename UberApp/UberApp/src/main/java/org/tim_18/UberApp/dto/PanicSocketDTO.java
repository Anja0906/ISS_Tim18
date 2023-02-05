package org.tim_18.UberApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.model.Panic;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicSocketDTO {
    private Integer id;
    private String user;
    private String time;
    @Length(max=500)
    private String reason;

    public PanicSocketDTO(Panic panic) {
        this(panic.getId(), panic.getUser().getEmail(),
                 panic.getTime().toString(),
                panic.getReason());
    }
}
