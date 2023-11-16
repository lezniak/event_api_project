package com.example.praca.controller;

import com.example.praca.dto.payment.PaymentDetailsDTO;
import com.example.praca.service.EventPaymentService;
import com.example.praca.service.PaymentService;
import com.example.praca.service.ReturnService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class EventPaymentController {

    private  final EventPaymentService EVENT_PAYMENT_SERVICE;
    private final String CANCEL_URL = "http://127.0.0.1:8082/event-api/payment/cancel";
    private final String SUCCESS_URL = "http://127.0.0.1:8082/event-api/payment/success";

    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
    @PostMapping("/pay")
    public String payment(@RequestBody PaymentDetailsDTO paymentDetailsDTO) {
        try {

            Payment payment = EVENT_PAYMENT_SERVICE.createPayment(paymentDetailsDTO, CANCEL_URL, SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    EVENT_PAYMENT_SERVICE.savePaymentAttemp(paymentDetailsDTO, payment, getTokenFromUrl(link.getHref()));
                    return link.getHref() + "?eventId=" + paymentDetailsDTO.getEventId();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "";
    }

    @GetMapping(value = "cancel")
    public String cancelPayment() {
        return "cancel";
    }

    @GetMapping(value = "success")
    public ReturnService successPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request) {
        try {
            Payment payment = EVENT_PAYMENT_SERVICE.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                String url = request.getRequestURL().toString() + "?" + request.getQueryString();
                return EVENT_PAYMENT_SERVICE.savePayment(paymentId, payerId, getTokenFromUrl(url));
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String getTokenFromUrl(String link) {
        int tokenStart = link.indexOf("token") + 6;
        return link.substring(tokenStart);
    }
}
