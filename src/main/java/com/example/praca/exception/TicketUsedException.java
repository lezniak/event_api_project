package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class TicketUsedException extends RuntimeException {
    private static final String MSG = "Ticket used exception";

    public TicketUsedException() {
        super(MSG);
    }
}
