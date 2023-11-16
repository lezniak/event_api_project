package com.example.praca.service;

import com.example.praca.dto.common.IdDto;
import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.filter.EventFilterParam;
import com.example.praca.dto.ticket.TicketFilterParam;
import com.example.praca.dto.ticket.TicketInformationDto;
import com.example.praca.exception.*;
import com.example.praca.model.Event;
import com.example.praca.model.EventMembers;
import com.example.praca.model.Ticket;
import com.example.praca.model.User;
import com.example.praca.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
@Slf4j
public class TicketService {
    private final EventService EVENT_SERVICE;
    private final UserService USER_SERVICE;
    private final EventMemberService EVENT_MEMBERS_SERVICE;
    private final TicketRepository TICKET_REPOSITORY;

    public byte[] createTicket(IdDto idDto) {
        Event event = EVENT_SERVICE.findEventById(idDto.getId());
        if (event.isHistory())
            throw new EventFinishedException();

        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
        EventMembers eventMembers = EVENT_MEMBERS_SERVICE.getEventMemberByUserIdAndEventId(user.getId(), idDto.getId());
        //check if user is accepted
        if (!eventMembers.isAccepted())
            throw new UserNotAcceptedException();

        try {
            byte[] ticketContent = createTicket(idDto.getId(), user.getId());
            if (ticketExist(ticketContent)) {
//                return ReturnService.returnInformation("", TicketInformationDto.of(TICKET_REPOSITORY.findTicketByContent(ticketContent).get()),1);
                TicketInformationDto dto = TicketInformationDto.of(TICKET_REPOSITORY.findTicketByContent(ticketContent).get());
                return dto.getTicketContent();
            }
           Ticket ticket =  TICKET_REPOSITORY.save(Ticket.createTicket(user, event, createTicket(idDto.getId(), user.getId())));
//            return ReturnService.returnInformation("Succ. create ticket", TicketInformationDto.of(ticket), 1);
            TicketInformationDto dto = TicketInformationDto.of(ticket);
            return dto.getTicketContent();
        } catch (Exception ex) {
            log.error("Err. create ticket for user:  " + user.getId() + ": " + ex.getMessage());
//            return ReturnService.returnError("Err. create ticket: " + ex.getMessage(), -1);
            throw ex;
        }
    }

    public ReturnService checkTicket(byte[] content) {
        Ticket ticket = getTicketByContent(content);

        if (ticket.getUsed())
            return ReturnService.returnInformation("Ticket used", 0);
        ticket.setUsed(true);
        TICKET_REPOSITORY.save(ticket);
        return ReturnService.returnInformation("Ticket checked", 1);
    }

    private boolean ticketExist(byte[] content) {
        return TICKET_REPOSITORY.findTicketByContent(content).isPresent();
    }


   private byte[] createTicket(Long eventId, Long userId) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(createTicketContent(eventId, userId), BarcodeFormat.QR_CODE, 250, 250);
                //save into db
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
                return bos.toByteArray();

            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
   }

   private String createTicketContent(Long eventId, Long userId) {
            return "eventId:" + eventId + ";userId:" + userId;
   }

   public Ticket getTicketByContent(byte[] content) {
        return TICKET_REPOSITORY.findTicketByContent(content).orElseThrow(() -> new TicketNotExistException());
   }

    public ReturnService getAllTickets(int pageNo, int pageSize, String sortBy, String sortDir, TicketFilterParam filterParam, String... value) {
        Long userId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        String used = value[0];
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Ticket> ticketPage = null;
        switch (filterParam) {
            case AU:
                if (Boolean.valueOf(used)) {
                    ticketPage = TICKET_REPOSITORY.findTicketByUserIdUsed(userId, pageable);
                } else {
                    ticketPage = TICKET_REPOSITORY.findTicketByUserId(userId, pageable);
                }
                break;
            case AUE:
                Long eventId = Long.valueOf(value[1]);
                if (Boolean.valueOf(used)) {
                    ticketPage = TICKET_REPOSITORY.findTicketByUserIdAndEventIdUsed(userId, eventId, pageable);
                } else {
                    ticketPage = TICKET_REPOSITORY.findTicketByUserIdAndEventId(userId, eventId, pageable);
                }
                break;
            case AE:
                Long eventIdAll = Long.valueOf(value[1]);
                Long eventOwnerId = EVENT_SERVICE.getEventOwnerId(eventIdAll);
                if (!EVENT_SERVICE.isEventOwner(eventOwnerId))
                    throw new InvalidEventOwnerException();
                if (Boolean.valueOf(used)) {
                    ticketPage = TICKET_REPOSITORY.findTicketByEventIdUsed(eventIdAll, pageable);
                } else {
                    ticketPage = TICKET_REPOSITORY.findTicketByEventId(eventIdAll, pageable);
                }
                break;
        }

        if (ticketPage == null)
            throw new TicketNotExistException();
        List<Ticket> ticketList = ticketPage.getContent();

        List<TicketInformationDto> informationDtoList = ticketList.stream()
                .map(x -> TicketInformationDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(informationDtoList, ticketPage), 1);

    }
}
