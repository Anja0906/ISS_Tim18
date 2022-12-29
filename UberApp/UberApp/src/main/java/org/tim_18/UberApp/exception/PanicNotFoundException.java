package org.tim_18.UberApp.exception;

public class PanicNotFoundException extends RuntimeException{
    public PanicNotFoundException(String message) {
        super(message);
    }
}

