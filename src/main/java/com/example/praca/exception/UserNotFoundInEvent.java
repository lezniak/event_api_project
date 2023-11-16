package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class UserNotFoundInEvent extends RuntimeException{
    private static final String MSG = "User not found in event";

    public UserNotFoundInEvent() {
        super(MSG);
    }
}
