package org.tim_18.UberApp.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String accessToken;
    private String refreshToken;

    public LoginResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
