package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class RoleNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find role";

    public RoleNotFoundException() {
        super(MSG);
    }
}
