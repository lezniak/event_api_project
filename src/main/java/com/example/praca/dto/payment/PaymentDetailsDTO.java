package com.example.praca.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class PaymentDetailsDTO {
    private String price;
    private String currency;
    private String method;
    private String intent;
    private String description;
    private Long eventId;
}
