package org.tim_18.UberApp.exception;

public class UserActivationNotFoundException extends  RuntimeException{
    public UserActivationNotFoundException(String message) {
        super(message);
    }
}
