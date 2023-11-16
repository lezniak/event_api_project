package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class UserNotAcceptedException extends RuntimeException {
    private static final String MSG = "User not accepted";

    public UserNotAcceptedException() {
        super(MSG);
    }
}
