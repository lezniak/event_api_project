package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class NullValueException extends RuntimeException{
    private static final String MSG = "Value cannot be null";

    public NullValueException() {
        super(MSG);
    }
}
