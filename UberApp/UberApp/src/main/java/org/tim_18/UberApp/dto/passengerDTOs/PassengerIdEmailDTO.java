package org.tim_18.UberApp.dto.passengerDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerIdEmailDTO {

    @NotNull
    private Integer id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
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
