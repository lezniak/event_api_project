package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */

public class EventAddressNotFoundException extends RuntimeException {
    private static final String MSG = "Event address not found";

    public EventAddressNotFoundException() {
        super(MSG);
    }
}
