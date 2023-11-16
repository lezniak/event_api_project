package com.example.praca.dto.event;

import com.example.praca.model.Event;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class SimpleEventInformation {
    private String name;
    private String ownerName;
    private String startDate;
    private String eventDescription;


    public static SimpleEventInformation of(Event event) {
        SimpleEventInformation dto = new SimpleEventInformation();

        dto.setName(event.getName());
        dto.setOwnerName(dto.getOwnerName());
        dto.setStartDate(dto.getStartDate());
        dto.setEventDescription(dto.getEventDescription());

        return dto;
    }
}
