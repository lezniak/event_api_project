package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class TicketExistException extends RuntimeException {
    private static final String MSG = "Ticket already exist";

    public TicketExistException() {
        super(MSG);
    }
}
