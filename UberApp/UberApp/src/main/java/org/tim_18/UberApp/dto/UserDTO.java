package org.tim_18.UberApp.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.User;

import java.util.HashSet;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
    private boolean blocked;

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
    }

    public UserDTO(User user) {
        this.id              = user.getId();
        this.name            = user.getName();
        this.surname         = user.getSurname();
        this.profilePicture  = user.getProfilePicture();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email           = user.getEmail();
        this.address         = user.getAddress();
        this.blocked         = user.isBlocked();
    }

    public HashSet<UserDTO> makeUserDTOS(Page<User> users){
        HashSet<UserDTO> usersDTO = new HashSet<>();
        for (User user:users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }




}
