package org.tim_18.UberApp.dto;

import lombok.Data;
import org.tim_18.UberApp.model.User;

@Data
public class UserSimpleDTO {
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserSimpleDTO() {}

    public UserSimpleDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }

    public UserSimpleDTO(User user) {
        this(user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(), user.getEmail(), user.getAddress());
    }
}
