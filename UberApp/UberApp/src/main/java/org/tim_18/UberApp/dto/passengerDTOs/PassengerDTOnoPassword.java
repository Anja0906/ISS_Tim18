package org.tim_18.UberApp.dto.passengerDTOs;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Passenger;

import java.util.ArrayList;
import java.util.List;

@Data
public class PassengerDTOnoPassword {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
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
