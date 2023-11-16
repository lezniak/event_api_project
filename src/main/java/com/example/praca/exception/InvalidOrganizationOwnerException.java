package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class InvalidOrganizationOwnerException extends RuntimeException {
    private static final String MSG = "Invalid organization owner";

    public InvalidOrganizationOwnerException() {
        super(MSG);
    }
}
