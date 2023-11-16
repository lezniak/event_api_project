package com.example.praca.service;

import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.exception.QrCodeNotFoundException;
import com.example.praca.model.QrCode;
import com.example.praca.model.User;
import com.example.praca.repository.QrCodeEventRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Daniel Lezniak
 */

@Service
@AllArgsConstructor
public class QrCodeService {

    private final UserService USER_SERVICE;
    private final QrCodeEventRepository QR_CODE_REPOSITORY;
    private final String qrMessage = "<a href='http://146.59.3.226:8080/event-api/event?eventId=%s'> Dołącz do wydarzenia</a>";
//    public void generateEventQrCode(Long eventId) {
//        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
//        if (!EVENT_SERVICE.isEventOwner(user.getId()))
//            throw new InvalidEventOwnerException();
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            BitMatrix bitMatrix = qrCodeWriter.encode(createQrEventMessage(eventId.toString()), BarcodeFormat.QR_CODE, 250, 250);
//            //save into db
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
//            saveQrCodeInDb(bos.toByteArray(), user, eventId);
//
//        } catch (WriterException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void saveQrCodeInDb(byte[] image, User user, Long eventId) {
        QrCode qrCode = new QrCode();
        qrCode.setContent(image);
        qrCode.setUser(user);
        qrCode.setEventId(eventId);

        QR_CODE_REPOSITORY.save(qrCode);
    }

    private String createQrEventMessage(String message) {
        return String.format(qrMessage, message);
    }

//    public Resource getQrCodeEvent(Long eventId) {
//        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
//        if (!EVENT_SERVICE.isEventOwner(user.getId()))
//            throw new InvalidEventOwnerException();
//        QrCode qrCode = findByEventId(eventId);
//        return new ByteArrayResource(qrCode.getContent());
//    }

    private QrCode findByEventId(Long eventId) {
        return QR_CODE_REPOSITORY.findByEventId(eventId).orElseThrow(() -> new QrCodeNotFoundException());
    }

}
