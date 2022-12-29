package org.tim_18.UberApp.exception;

public class VehicleNotFoundException extends  RuntimeException{
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
