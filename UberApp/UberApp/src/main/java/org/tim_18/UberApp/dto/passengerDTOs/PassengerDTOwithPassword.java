package org.tim_18.UberApp.dto.passengerDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Passenger;

@Data
public class PassengerDTOwithPassword {

    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
    private String password;

    public PassengerDTOwithPassword() {
    }

    public PassengerDTOwithPassword(Passenger passenger){
        this(passenger.getId(), passenger.getName(), passenger.getName(), passenger.getProfilePicture(), passenger.getTelephoneNumber(), passenger.getEmail(), passenger.getAddress(), passenger.getPassword());
    }

    public PassengerDTOwithPassword(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.id = id;
    }
}
