package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class AlreadyJoinEventException extends RuntimeException{
    private final static String MSG = "Already join event";

    public AlreadyJoinEventException() {
        super(MSG);
    }
}
