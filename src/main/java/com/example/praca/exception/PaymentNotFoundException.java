package com.example.praca.exception;

import com.paypal.api.payments.Payment;

/**
 * @author Daniel Lezniak
 */
public class PaymentNotFoundException extends RuntimeException {
    private static final String MSG = "Payment not found";

    public PaymentNotFoundException() {
        super(MSG);
    }
}
