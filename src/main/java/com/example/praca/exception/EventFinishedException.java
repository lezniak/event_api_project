package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class EventFinishedException extends RuntimeException{
    private static final String MSG = "Event finished";

    public EventFinishedException() {
        super(MSG);
    }
}
