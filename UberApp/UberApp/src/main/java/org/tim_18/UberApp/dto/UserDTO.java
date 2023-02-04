package org.tim_18.UberApp.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.tim_18.UberApp.model.Role;
import org.tim_18.UberApp.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    private boolean isActive;
    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, List<String> roles,boolean isActive) {
        this.id                 = id;
        this.name               = name;
        this.surname            = surname;
        this.profilePicture     = profilePicture;
        this.telephoneNumber    = telephoneNumber;
        this.email              = email;
        this.address            = address;
        this.roles              = roles;
        this.isActive           = isActive;
    }

    public UserDTO(User user, List<Role> roles) {
        this.id              = user.getId();
        this.name            = user.getName();
        this.surname         = user.getSurname();
        this.profilePicture  = user.getProfilePicture();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email           = user.getEmail();
        this.address         = user.getAddress();
        this.blocked         = user.isBlocked();
        List<String> rolesStr = new ArrayList<>();
        for (Role role:roles) {
            rolesStr.add(role.getName());
        }
        this.roles         = rolesStr;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.profilePicture = user.getProfilePicture();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.blocked = user.isBlocked();
        this.isActive= user.isActive();
    }

    public HashSet<UserDTO> makeUserDTOS(Page<User> users){
        HashSet<UserDTO> usersDTO = new HashSet<>();
        for (User user:users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }


}
