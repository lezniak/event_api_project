package com.example.praca.dto.ticket;

import com.example.praca.model.Ticket;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Daniel Lezniak
 */
@Data
public class TicketInformationDto {
    private Long ticketId;
    private Long eventId;
    private String eventName;
    private Timestamp eventStartDate;
    private String ownerName;
    private byte[] ticketContent;
    private boolean used;


    public static TicketInformationDto of(Ticket ticket) {
        TicketInformationDto dto = new TicketInformationDto();

        dto.setTicketId(ticket.getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setEventName(ticket.getEvent().getName());
        dto.setEventStartDate(ticket.getEvent().getStartDate());
        dto.setOwnerName(ticket.getUser().getName());
        dto.setTicketContent(ticket.getContent());
        dto.setUsed(ticket.getUsed());

        return dto;
    }
}
