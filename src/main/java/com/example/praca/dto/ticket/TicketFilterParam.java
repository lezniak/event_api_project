package com.example.praca.dto.ticket;

/**
 * @author Daniel Lezniak
 */
public enum TicketFilterParam {
    AU("All user ticket"),
    AUE("All user in event"),
    AE("All in event");

    public final String name;
    private TicketFilterParam(String name) {
        this.name = name;
    }
}
