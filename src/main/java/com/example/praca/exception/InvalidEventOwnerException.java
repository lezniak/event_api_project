package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class InvalidEventOwnerException extends RuntimeException{
    private static final String MSG = "Not your event";

    public InvalidEventOwnerException() {
        super(MSG);
    }
}
