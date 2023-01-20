package org.tim_18.UberApp.dto.passengerDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerDTOwithPassword {

    private Integer id;

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

    @NotNull
    @Length(min = 2, message = "Bad password format")
    private String password;

    public PassengerDTOwithPassword() {}
    public PassengerDTOwithPassword(Passenger passenger){
        this(passenger.getId(), passenger.getName(),
             passenger.getName(), passenger.getProfilePicture(),
             passenger.getTelephoneNumber(), passenger.getEmail(),
             passenger.getAddress(), passenger.getPassword());
    }

    public PassengerDTOwithPassword(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
        this.password           = password;
        this.id                 = id;
    }
}
