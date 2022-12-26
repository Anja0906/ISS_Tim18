package org.tim_18.UberApp.exception;

public class LocationForRideNotFoundException extends RuntimeException{
    public LocationForRideNotFoundException(String message) {
        super(message);
    }
}
