package com.example.praca.controller;

import com.example.praca.dto.newsletter.EventInformationDto;
import com.example.praca.service.EventService;
import com.example.praca.service.QrCodeService;
import com.example.praca.service.ReturnService;
import com.example.praca.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/qr-code")
public class QrCodeController {
    private final QrCodeService QR_CODE_SERVICE;


//    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
//    @PostMapping("")
//    public void createQrCodeEvent(@RequestParam Long eventId) {
//        QR_CODE_SERVICE.generateEventQrCode(eventId);
//    }

//    @PreAuthorize("isAuthenticated() && hasAuthority('USER')")
//    @GetMapping(value = "", produces = MediaType.IMAGE_JPEG_VALUE)
//    public Resource getQrCodeEvent(@RequestParam Long eventId) {
//       return QR_CODE_SERVICE.getQrCodeEvent(eventId);
//    }

}
