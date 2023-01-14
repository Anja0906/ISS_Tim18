package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerEmailDTO {
    private Integer id;
    private String email;

    public PassengerEmailDTO(){}

    public PassengerEmailDTO(Passenger passenger) {
        this(passenger.getId(), passenger.getEmail());
    }

    public PassengerEmailDTO(Integer id, String email) {
        this.id     = id;
        this.email  = email;
    }
}
