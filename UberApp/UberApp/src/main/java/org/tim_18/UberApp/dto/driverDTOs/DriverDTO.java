package org.tim_18.UberApp.dto.driverDTOs;


import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Page;

import org.tim_18.UberApp.model.Driver;

import java.util.HashSet;
@Data
public class DriverDTO{

    
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public DriverDTO() {}

    public DriverDTO(Driver driver) {
        this(driver.getId(), driver.getName(),
             driver.getSurname(), driver.getProfilePicture(),
             driver.getTelephoneNumber(),driver.getEmail(),
             driver.getAddress());
    }


    public DriverDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.id              = id;
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
    }

    public HashSet<DriverDTO> makeDriversDTO(Page<Driver> drivers){
        HashSet<DriverDTO> driverDTOS = new HashSet<>();
        for(Driver driver: drivers){
            driverDTOS.add(new DriverDTO(driver));
        }
        return driverDTOS;
    }
}
