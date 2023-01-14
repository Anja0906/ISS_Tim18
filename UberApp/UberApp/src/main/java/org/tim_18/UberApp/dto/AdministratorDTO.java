package org.tim_18.UberApp.dto;

import lombok.Data;

@Data
public class AdministratorDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String profilePicture;

    public AdministratorDTO() {}

    public AdministratorDTO(String username, String password, String name, String surname, String profilePicture) {
        this.username       = username;
        this.password       = password;
        this.name           = name;
        this.surname        = surname;
        this.profilePicture = profilePicture;
    }
}
