package org.tim_18.UberApp.dto.driverDTOs;

<<<<<<< Updated upstream
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
>>>>>>> Stashed changes
import org.tim_18.UberApp.model.Driver;

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
        this(driver.getName(), driver.getSurname(), driver.getProfilePicture(),
                driver.getTelephoneNumber(),driver.getEmail(), driver.getPassword(), driver.getAddress());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
