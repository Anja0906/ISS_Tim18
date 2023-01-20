package org.tim_18.UberApp.dto;

import lombok.Data;

@Data
public class changePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
