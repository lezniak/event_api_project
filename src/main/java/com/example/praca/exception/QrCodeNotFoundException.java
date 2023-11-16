package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class QrCodeNotFoundException extends RuntimeException{
    private static final String MSG = "Qr code not found exception";

    public QrCodeNotFoundException() {
        super(MSG);
    }
}
