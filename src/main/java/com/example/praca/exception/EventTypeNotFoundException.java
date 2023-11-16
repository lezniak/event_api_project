package com.example.praca.exception;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daniel Lezniak
 */
public class EventTypeNotFoundException extends RuntimeException {
    private static final String MSG = "Event type not found exception";

    public EventTypeNotFoundException() {
        super(MSG);
    }
}
