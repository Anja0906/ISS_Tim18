package org.tim_18.UberApp.model;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String accessToken;

    private String refreshToken;
    private int expiresIn;
    private String type = "Bearer";
    private Integer id;
    private String email;
    private String details;
    private List<String> roles;

    public JwtResponse(String token, int expiresIn, Integer id, String email, List<String> roles, String details) {
        this.accessToken    = token;
        this.refreshToken   = token;
        this.expiresIn      = expiresIn;
        this.id             = id;
        this.email          = email;
        this.roles          = roles;
        this.details        = details;
    }
}
