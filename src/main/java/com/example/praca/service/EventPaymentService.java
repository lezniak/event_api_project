package com.example.praca.service;

import com.example.praca.dto.payment.PaymentDetailsDTO;
import com.example.praca.exception.AlreadyPayedException;
import com.example.praca.exception.PaymentNotFoundException;
import com.example.praca.exception.UserNotFoundInEvent;
import com.example.praca.model.Event;
import com.example.praca.model.EventPayment;
import com.example.praca.model.User;
import com.example.praca.repository.EventPaymentRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
@Slf4j
public class EventPaymentService {
    private final EventPaymentRepository EVENT_PAYMENT_REPOSITORY;
    private final UserService USER_SERVICE;
    private final EventService EVENT_SERVICE;

//    @Autowired
//    private APIContext apiContext;

    public Payment createPayment(PaymentDetailsDTO paymentDetailsDTO, String cancelUrl, String successUrl) throws PayPalRESTException {
        Long userId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        if (!EVENT_SERVICE.isMemberOf(paymentDetailsDTO.getEventId(), userId))
            throw new UserNotFoundInEvent();

        if (userPayed(paymentDetailsDTO.getEventId(), userId))
            throw new AlreadyPayedException();

        Amount amount = new Amount();
        amount.setCurrency(paymentDetailsDTO.getCurrency());
        amount.setTotal(paymentDetailsDTO.getPrice());

        Transaction transaction = new Transaction();
        transaction.setDescription(paymentDetailsDTO.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(paymentDetailsDTO.getMethod());

        Payment payment = new Payment();
        payment.setIntent(paymentDetailsDTO.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

//        return payment.create(apiContext);
        return null;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
//        return payment.execute(apiContext, paymentExecute);
        return null;
    }

    public ReturnService savePaymentAttemp(PaymentDetailsDTO paymentDetailsDTO, Payment payment, String token) {
        Event event = EVENT_SERVICE.findEventById(paymentDetailsDTO.getEventId());
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());

        String paymentLink = "";
        for(Links link:payment.getLinks()) {
            if(link.getRel().equals("approval_url")) {
                paymentLink = link.getHref();
            }
        }
        EventPayment eventPayment = new EventPayment();
        eventPayment.setEventPayment(event);
        eventPayment.setUserPayment(user);
        eventPayment.setPrice(Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal()));
        eventPayment.setDescription(payment.getTransactions().get(0).getDescription());
        eventPayment.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
        eventPayment.setSuccess(false);
        eventPayment.setPaymentLink(paymentLink);
        eventPayment.setPaymentToken(token);


        try {
            EVENT_PAYMENT_REPOSITORY.save(eventPayment);
        } catch (Exception ex) {
            log.error("Err: saving payment " + ex.getMessage());
        }
        return null;
    }

    public ReturnService savePayment(String paymentId, String payerId, String token) {
        String correctToken = token.substring(0, token.indexOf('&'));
        EventPayment eventPayment = findEventPaymentByToken(correctToken);
        eventPayment.setPaymentId(paymentId);
        eventPayment.setPayerId(payerId);
        eventPayment.setSuccess(true);
        try {
            EVENT_PAYMENT_REPOSITORY.save(eventPayment);
            return ReturnService.returnInformation("succ. update payment", 1);
        } catch (Exception ex) {
            log.error("Err: update payment: " + ex.getMessage());
            return ReturnService.returnError("err: update " + ex.getMessage(), -1);
        }
    }

    private Optional<EventPayment> findEventPaymentByUserIdAndEventIdAndPayed(Long userId, Long eventId) {
        return EVENT_PAYMENT_REPOSITORY.findByUserIdAndEventId(userId, eventId);
    }
    private boolean userPayed(Long eventId, Long userId) {
        return findEventPaymentByUserIdAndEventIdAndPayed(userId, eventId).isPresent();
    }

    private EventPayment findEventPaymentByToken(String token) {
        return EVENT_PAYMENT_REPOSITORY.findByToken(token).orElseThrow(() -> new PaymentNotFoundException());
    }
}
