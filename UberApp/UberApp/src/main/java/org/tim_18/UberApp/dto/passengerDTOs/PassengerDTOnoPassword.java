package org.tim_18.UberApp.dto.passengerDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Passenger;

import java.util.ArrayList;
import java.util.List;

@Data
public class PassengerDTOnoPassword {
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

    public PassengerDTOnoPassword() {}

    public PassengerDTOnoPassword(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
    }

    public PassengerDTOnoPassword(Passenger passenger){
        this(passenger.getId(), passenger.getName(),
             passenger.getSurname(), passenger.getProfilePicture(),
             passenger.getTelephoneNumber(), passenger.getEmail(),
             passenger.getAddress());
    }

    public static List<PassengerDTOnoPassword> getPassengersDTO(Page<Passenger> passengers) {
        List<PassengerDTOnoPassword> passengersDTO = new ArrayList<>() ;
        for (Passenger p : passengers) {
            passengersDTO.add(new PassengerDTOnoPassword(p));
        }
        return passengersDTO;
    }
}
