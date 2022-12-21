package org.tim_18.UberApp.dto;


import lombok.Data;
import org.tim_18.UberApp.model.User;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
    private String password;
    private boolean blocked;
    private boolean active;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getSurname(), user.getProfilePicture(), user.getTelephoneNumber(), user.getEmail(), user.getAddress(), user.getPassword(), user.isBlocked(), user.isActive());
    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        this.id              = id;
        this.name            = name;
        this.surname         = surname;
        this.profilePicture  = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email           = email;
        this.address         = address;
        this.password        = password;
        this.blocked         = blocked;
        this.active          = active;
    }

    public UserDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password, boolean blocked, boolean active) {
    }

    public UserDTO(Integer id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address, String password) {

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

}
