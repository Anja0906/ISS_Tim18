package org.tim_18.UberApp.dto.passengerDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerEmailDTO {
    private String email;

    public PassengerEmailDTO(){}

    public PassengerEmailDTO(Passenger passenger) {
        this(passenger.getEmail());
    }

    public PassengerEmailDTO(String email) {
        this.email  = email;
    }
}