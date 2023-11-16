package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class OrganizationAlreadyExistException extends RuntimeException {
    private static final String MSG = "Organization already exist in Event";

    public OrganizationAlreadyExistException() {
        super(MSG);
    }
}
