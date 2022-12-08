package org.tim_18.UberApp.dto;

import org.tim_18.UberApp.model.User;

public class LoginDTO {
    private String email;
    private String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginDTO(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
