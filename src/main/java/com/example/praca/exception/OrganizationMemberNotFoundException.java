package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class OrganizationMemberNotFoundException extends RuntimeException {
    private static final String MSG = "orgaization member not found";

    public OrganizationMemberNotFoundException() {
        super(MSG);
    }
}
