package org.tim_18.UberApp.dto.driverDTOs;

import lombok.Data;
import org.tim_18.UberApp.model.Driver;
@Data
public class DriverDTOWithoutId {
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String password;
    private String address;

    public DriverDTOWithoutId() {}

    public DriverDTOWithoutId(Driver driver) {
        this(driver.getName(), driver.getSurname(),
             driver.getProfilePicture(), driver.getTelephoneNumber(),
             driver.getEmail(), driver.getPassword(),
             driver.getAddress());
    }
    public DriverDTOWithoutId(String name, String surname, String profilePicture, String telephoneNumber, String email, String password,String address) {
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.password        = password;
        this.address         = address;
    }
}
