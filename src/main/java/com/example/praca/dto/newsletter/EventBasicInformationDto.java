package com.example.praca.dto.newsletter;

import com.example.praca.model.Event;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventBasicInformationDto {
    private String eventName;
    private Long eventId;
    private String ownerName;
    private String startData;
    private String eventCity;

    public static EventBasicInformationDto of (Event event) {
        EventBasicInformationDto dto = new EventBasicInformationDto();

        dto.setEventName(event.getName());
        dto.setEventId(event.getId());
        dto.setOwnerName(event.getUser().getName());
        dto.setStartData(event.getStartDate().toString());
        dto.setEventCity(event.getEvent_address().getCity());

        return dto;
    }
}
