package com.example.praca.controller;

import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
public class MainController {
    private final EmailService emailService;

    @GetMapping("")
    public String main() {
        return "It's work!";
    }

}
