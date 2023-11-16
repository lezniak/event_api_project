package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class SessionNotFoundException extends RuntimeException{
    private static final String MSG = "Session not found";

    public SessionNotFoundException() {
        super(MSG);
    }
}
