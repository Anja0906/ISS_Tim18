package org.tim_18.UberApp.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.model.User;

import java.util.HashSet;
import java.util.List;

@Data
public class UserDTOwithPassword {

    private Integer id;
    @Length(max = 100)
    private String name;
    @Length(max = 100)
    private String surname;
    private String profilePicture;
    @Length(max = 18)
    private String telephoneNumber;
    @Length(max = 100)
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    private String email;
    @Length(max = 100)
    private String address;

    private List<String> roles;
    private String password;
    public UserDTOwithPassword() {
    }

    public UserDTOwithPassword(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
        this.password           = password;
    }

    public UserDTOwithPassword(User user) {
        this.id              = user.getId();
        this.name            = user.getName();
        this.surname         = user.getSurname();
        this.profilePicture  = user.getProfilePicture();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email           = user.getEmail();
        this.address         = user.getAddress();
        this.password        = user.getPassword();
    }

    public HashSet<UserDTOwithPassword> makeUserDTOS(Page<User> users){
        HashSet<UserDTOwithPassword> usersDTO = new HashSet<>();
        for (User user:users) {
            usersDTO.add(new UserDTOwithPassword(user));
        }
        return usersDTO;
    }
}
