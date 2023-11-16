package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class TokenNotFoundException extends RuntimeException{
    private static final String MSG = "Can't find token";

    public TokenNotFoundException() {
        super(MSG);
    }
}
