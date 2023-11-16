package com.example.praca.dto.payment;

import lombok.Builder;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
@Builder
public class SavePaymentDTO {
    private String paymentId;
    private String payerId;
    private PaymentDetailsDTO paymentDetailsDTO;
}
