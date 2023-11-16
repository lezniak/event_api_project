package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class EventNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find event";

    public EventNotFoundException() {
        super(MSG);
    }
}
