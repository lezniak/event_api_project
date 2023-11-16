package com.example.praca.dto.eventtype;

import com.example.praca.dto.event.InformationEventDto;
import com.example.praca.dto.user.InformationUserDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
@Builder
public class EventTypeEventUserDto {
    private EventTypeInformationDto eventTypeInformationDto;
    private List<InformationEventDto> informationEventDtos;
    private List<InformationUserDto> informationUserDtos;
}
