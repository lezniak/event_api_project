package com.example.praca.exception;

import com.example.praca.model.Organization;

/**
 * @author Daniel Lezniak
 */
public class OrganizationNotFoundException extends RuntimeException {
    private static final String MSG = "Organization not found";

    public OrganizationNotFoundException() {
        super(MSG);
    }
}
