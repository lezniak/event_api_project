package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class AlreadyPayedException extends RuntimeException {
    private static final String MSG = "already payed";

    public AlreadyPayedException() {
        super(MSG);
    }
}
