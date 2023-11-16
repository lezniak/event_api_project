package com.example.praca.dto.eventtype;

import com.example.praca.model.EventType;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventTypeInformationDto {
    private String name;
    private Long id;

    public static EventTypeInformationDto of(EventType eventType) {
        EventTypeInformationDto dto = new EventTypeInformationDto();

        dto.setName(eventType.getName());
        dto.setId(eventType.getId());

        return dto;
    }
}
