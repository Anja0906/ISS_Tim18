package org.tim_18.UberApp.dto.driverDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.Driver;
@Data
public class DriverDTOWithoutId {
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
