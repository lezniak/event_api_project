package com.example.praca.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Daniel Lezniak
 */
@Data
@AllArgsConstructor
public class ExceptionDetails {
    private Date timestamp;
    private String message;
    private String details;
}
