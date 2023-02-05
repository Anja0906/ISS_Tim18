package org.tim_18.UberApp.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.tim_18.UberApp.model.User;
@Data
public class LoginDTO {
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad email format")
    private String email;

    private String password;

    public LoginDTO(String email, String password) {
        this.email      = email;
        this.password   = password;
    }

    public LoginDTO(User user){
        this.email      = user.getEmail();
        this.password   = user.getPassword();
    }
}
