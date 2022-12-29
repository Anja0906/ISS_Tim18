package org.tim_18.UberApp.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.model.User;

import java.util.HashSet;
import java.util.List;

@Data
public class UserDTOwithPassword {

    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    private String password;
    private List<Role> roles;

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
