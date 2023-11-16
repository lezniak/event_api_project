package com.example.praca.service;

import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.newsletter.EventBasicInformationDto;
import com.example.praca.dto.user.InformationUserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    private static final String EMAIL_FROM = "Whatssup";

    private static String NEW_EVENTS_NEWSLETTER_MSG = "<!DOCTYPE html>" +
            "<html lang='pl'>" +
            "<head>" +
            "<meta charset='utf-8'>" +
            "</head>" +
            "<body>" +
            "<h2> Hej %s!</h2>" +
            "W okolicy są nowe wydarzenia, które mogą się wydawać interesujące! <br>" +
            "<h2>Rodzaj wydarzenia: %s!</h2><br>" +
            "<p>Nazwa: %s</p>" +
            "<p>Miejscowość: %s</p>" +
            "<p>Data: %s</p>" +
            "<p>Twórca: %s</p>" +
            "<hr>"+
            "Z pozdrowieniami, <br> " +
            "Zespół Whatssup!<br>" +
            "Kontakt: eventmailingv1@gmail.com" +
            "</body>" +
            "</html>";

    private static String MAIL_CHANGE_MSG = "<html>" +
                                                "<head>" +
                                                 "<meta charset='UTF-8'>" +
                                                "</head>" +
                                                " <body>" +
                                                "<h2> Hej %s!</h2>" +
                                                "<h3>Twój email przypisany do konta został zmieniony, proszę potwierdź zmiany. </h3> "+
//                                                "<a href='http://146.59.3.226:8082/user/confirm-mail?token=%s'> Potwierdź zmianę</a> " +
                                                "<a href='http://127.0.0.1:8082/user/confirm-mail?token=%s'> Potwierdź zmianę</a> " +
                                                "<hr>"+
                                                "Z pozdrowieniami, <br> " +
                                                "Zespół Whatssup!<br>" +
                                                 "Kontakt: eventmailingv1@gmail.com" +
                                                "</body>"+
                                                "</html>";

    private static String CHANGE_PASSWORD_MSG = "<html>" +
                                                "<head>" +
                                                "<meta charset='UTF-8'>" +
                                                "</head>" +
                                                "<body>" +
                                                "<h2> Hej %s!</h2>" +
                                                "<h2>Twoje hasło zostało zmienione, proszę potwierdzić zmiany</h2>" +
//                                                "<a href='localhost:8082/user/confirm-password?token=%s'<br>"+
                                                "<a href='http://127.0.0.1:8082/user/confirm-password?token=%s'<br>"+
                                                 "<hr>"+
                                            "Z pozdrowieniami, <br> " +
                                            "Zespół Whatssup!<br>" +
                                            "Kontakt: eventmailingv1@gmail.com" +
                                                "</body>" +
                                                "</html>";

    private static String REGISTRATION_MAIL = "<html>" +
                                                "<head>" +
                                                "<meta charset='UTF-8'>" +
                                                "</head>" +
                                                "<body> " +
                                                 "<h2> Hej %s!</h2>" +
                                                "<h2> Dziękujemy za rejestrację, proszę kliknąć w link aby aktywować konto </h2>" +
//                                                "<a href='http://146.59.3.226:8082/event-api/user/confirm?token=%s'>Aktywuj konto</a>" +
                                                "<a href='http://127.0.0.1:8082/event-api/user/confirm?token=%s'>Aktywuj konto</a>" +
                                                 "<hr>"+
                                                "Z pozdrowieniami, <br> " +
                                                "Zespół Whatssup!<br>" +
                                                "Kontakt: eventmailingv1@gmail.com" +
                                                "</body>" +
                                                "</html>";

    public EmailService(){};


    protected void sendConfirmationToken(String name, String userMail, String token) {
        String message = createMailMessageNewAccount(name, token);
        this.sendMail(userMail, "REGISTRATION", message);
    }

    protected void sendConfirmationTokenChangedPassword(String name, String userMail, String token) {
        String message = createMailMessageChangePassword(name, token);
        this.sendMail(userMail, "CHANGE PASSWORD", message);
    }

    protected void sendConfirmationTokenChangedEmail(String name, String userMail, String token) {
        String message = createMailMessageChangeEmail(name, token);
        this.sendMail(userMail, "CHANGE MAIL", message);
    }

    protected void sendNewEventsNewsletter(EventTypeInformationDto eventTypeInformationDto, List<EventBasicInformationDto> eventDtoList, List<InformationUserDto> userInformationDto) {
        for (EventBasicInformationDto eventDto : eventDtoList) {
            for (InformationUserDto userDto : userInformationDto) {
                String message = createMailMessage(userDto.getName(), eventTypeInformationDto, eventDto);
                this.sendMail(userDto.getEmail(), "NEWSLETTER", message);
            }
        }
    }

    private void sendMail(String recipient, String subject, String content) {
        javaMailSender.setDefaultEncoding("UTF-8");
        MimeMessage mail  = javaMailSender.createMimeMessage();
        try {
            mail.setContent(content, "text/html; charset=ISO-8859-2");
            MimeMessageHelper helper = new MimeMessageHelper(mail, "UTF-8");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setFrom(EMAIL_FROM);
            javaMailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String createMailMessage(String name, EventTypeInformationDto eventTypeInformationDto, EventBasicInformationDto informationEventDto) {
        return String.format(NEW_EVENTS_NEWSLETTER_MSG, name, eventTypeInformationDto.getName(), informationEventDto.getEventName(),  informationEventDto.getEventCity(), informationEventDto.getStartData(), informationEventDto.getOwnerName());
    }

    private String createMailMessageChangeEmail(String name, String token) {
        return String.format(MAIL_CHANGE_MSG, name, token);
    }

    private String createMailMessageChangePassword(String name, String token) {
        return String.format(CHANGE_PASSWORD_MSG, name, token);
    }

    private String createMailMessageNewAccount(String name, String token) {
        return String.format(REGISTRATION_MAIL, name, token);
    }

}

