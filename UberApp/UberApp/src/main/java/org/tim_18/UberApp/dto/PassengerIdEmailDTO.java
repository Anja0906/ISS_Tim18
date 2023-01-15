package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerIdEmailDTO {
    private Integer id;
    private String email;

    public PassengerIdEmailDTO(){}

    public PassengerIdEmailDTO(Passenger passenger) {
        this(passenger.getId(), passenger.getEmail());
    }

    public PassengerIdEmailDTO(Integer id, String email) {
        this.id     = id;
        this.email  = email;
    }
}
