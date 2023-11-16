package com.example.praca.dto.newletter;

import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.model.EventType;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventInformationByTypeDto {
    private EventType eventType;
    List<InformationEventDto> informationEventDtoList;
}
