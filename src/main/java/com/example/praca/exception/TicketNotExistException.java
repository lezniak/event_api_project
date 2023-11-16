package com.example.praca.exception;

import com.example.praca.model.Ticket;

/**
 * @author Daniel Lezniak
 */
public class TicketNotExistException extends RuntimeException {
    private static final String MSG = "Ticket not exist";

    public TicketNotExistException() {
        super(MSG);
    }
}
