package com.example.praca.dto.newsletter;

import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.dto.event.InformationEventDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
@Builder
public class EventInformationDto {
    private EventTypeInformationDto eventTypeInformationDto;
    private List<InformationEventDto> informationEventDtoList;
}
