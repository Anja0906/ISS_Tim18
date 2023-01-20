package org.tim_18.UberApp.exception;

public class FavoriteRideNotFoundException extends RuntimeException{
    public FavoriteRideNotFoundException(String message) {
        super(message);
    }
}
