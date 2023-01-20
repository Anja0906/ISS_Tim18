package org.tim_18.UberApp.dto.passengerDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Passenger;
@Data
public class PassengerDTO {
    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String profilePicture;

    @Length(min = 9, message = "Bad phone number format")
    private String telephoneNumber;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    private String email;

    @NotNull
    private String address;

    @Length(min = 2, message = "Bad password format")
    private String password;

    public PassengerDTO() {}

    public PassengerDTO(Passenger passenger){
        this(passenger.getName(), passenger.getName(), passenger.getProfilePicture(), passenger.getTelephoneNumber(), passenger.getEmail(), passenger.getAddress(), passenger.getPassword());
    }

    public PassengerDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
        this.password           = password;
    }
}
