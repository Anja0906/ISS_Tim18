package org.tim_18.UberApp.dto.panicDTOs;

import lombok.Data;
import org.tim_18.UberApp.dto.rideDTOs.RideRetDTO;
import org.tim_18.UberApp.dto.userDTOs.UserSimpleDTO;
import org.tim_18.UberApp.model.LocationsForRide;
import org.tim_18.UberApp.model.Panic;
import org.tim_18.UberApp.model.Ride;
import org.tim_18.UberApp.model.User;

import java.util.Date;
import java.util.Set;

@Data
public class PanicDTO {
    private Integer id;
    private UserSimpleDTO user;
    private RideRetDTO ride;
    private String time;
    private String reason;
    public PanicDTO(){}

    public PanicDTO(Integer id, User user, Ride ride, Date time, String reason, Set<LocationsForRide> locations) {
        this.id     = id;
        this.user   = new UserSimpleDTO(user);
        this.ride   = new RideRetDTO(ride, locations);
        this.time   = time.toString();
        this.reason = reason;
    }


    public PanicDTO(Panic panic, Set<LocationsForRide> locations) {
        this(panic.getId(), panic.getUser(),
             panic.getRide(), panic.getTime(),
             panic.getReason(), locations);
    }

}
