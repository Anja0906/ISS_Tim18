package org.tim_18.UberApp.Validation;

import lombok.Data;

@Data
public class ErrorMessage {
    private String message;

    public ErrorMessage(String message){
        this.message = message;
    }
}
