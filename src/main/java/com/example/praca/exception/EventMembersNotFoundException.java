package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class EventMembersNotFoundException extends RuntimeException{
    private static final String MSG = "Event members not found";

    public EventMembersNotFoundException() {
        super(MSG);
    }
}
