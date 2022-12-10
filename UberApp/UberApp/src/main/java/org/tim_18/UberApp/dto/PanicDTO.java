package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;

import java.util.Date;

@Data
public class PanicDTO {
    private Integer id;
    private UserSimpleDTO user;
    private Ride ride;
    private Date time;
    private String reason;
    public PanicDTO(){}

    public PanicDTO(Integer id, User user, Ride ride, Date time, String reason) {
        this.id = id;
        this.user = new UserSimpleDTO(user);
        this.ride = ride;
        this.time = time;
        this.reason = reason;
    }

    public PanicDTO(Panic panic) {
        this(panic.getId(), panic.getUser(), panic.getRide(), panic.getTime(), panic.getReason());
    }
}
