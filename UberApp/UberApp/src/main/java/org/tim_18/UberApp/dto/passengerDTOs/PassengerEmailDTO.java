package org.tim_18.UberApp.dto.passengerDTOs;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerEmailDTO {

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    private String email;

    public PassengerEmailDTO(){}

    public PassengerEmailDTO(Passenger passenger) {
        this(passenger.getEmail());
    }

    public PassengerEmailDTO(String email) {
        this.email  = email;
    }
}