package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class UserNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find user";

    public UserNotFoundException() {
        super(MSG);
    }
}
