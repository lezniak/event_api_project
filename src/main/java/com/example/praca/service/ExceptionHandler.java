package com.example.praca.service;


import com.example.praca.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * @author Daniel Lezniak
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidEventOwnerException.class, UserNotAcceptedException.class})
    public ResponseEntity<?> handleInvalidEventOwnerException(Exception exception, WebRequest request) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EventAddressNotFoundException.class, EventNotFoundException.class, RoleNotFoundException.class, TokenNotFoundException.class, UserNotFoundException.class, HobbyNotFoundException.class, EventTypeNotFoundException.class,
            UserNotFoundInEvent.class, EventMembersNotFoundException.class, SessionNotFoundException.class, EventFinishedException.class, QrCodeNotFoundException.class, OrganizationNotFoundException.class, TicketNotExistException.class})
    public ResponseEntity<?> handleEventAddressNotFoundException(Exception exception, WebRequest request) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullValueException.class)
    public ResponseEntity<?> handleNullValueException(Exception exception, WebRequest request) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NO_CONTENT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({AlreadyJoinEventException.class, OrganizationAlreadyExistException.class, InvalidOrganizationOwnerException.class, OrganizationMemberNotFoundException.class, TicketExistException.class,TicketUsedException.class})
    public ResponseEntity<?> handleAlreadyJoinEventException(Exception exception, WebRequest request) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionDetails, HttpStatus.CONFLICT);
    }

}
