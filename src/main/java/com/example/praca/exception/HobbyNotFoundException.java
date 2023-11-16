package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class HobbyNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find hobby";

    public HobbyNotFoundException() {
        super(MSG);
    }
}
