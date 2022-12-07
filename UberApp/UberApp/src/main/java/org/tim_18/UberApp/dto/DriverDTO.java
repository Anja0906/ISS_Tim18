package org.tim_18.UberApp.dto;

import jakarta.persistence.*;
import org.tim_18.UberApp.model.Driver;

import java.util.HashSet;

public class DriverDTO extends UserDTO{

    //{
    //  "id": 123,
    //  "name": "Pera",
    //  "surname": "Perić",
    //  "profilePicture": "U3dhZ2dlciByb2Nrcw==",
    //  "telephoneNumber": "+381123123",
    //  "email": "pera.peric@email.com",
    //  "address": "Bulevar Oslobodjenja 74"
    //}

    public DriverDTO() {}

    public DriverDTO(Driver driver) {
        super(driver);
    }

    public DriverDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        super(id, name, surname, profilePicture, telephoneNumber, email, address);


    }
}
