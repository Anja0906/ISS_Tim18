package org.tim_18.UberApp.dto.userDTOs;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.tim_18.UberApp.model.User;

@Data
public class UserSimpleDTO {
    @Length(max = 100)

    private String name;
    @Length(max = 100)

    private String surname;
    private String profilePicture;
    @Length(max = 18)

    private String telephoneNumber;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    @Length(max = 100)

    private String email;
    @Length(max = 100)

    private String address;

    public UserSimpleDTO() {}

    public UserSimpleDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
    }

    public UserSimpleDTO(User user) {
        this(user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(), user.getEmail(), user.getAddress());
    }
}
