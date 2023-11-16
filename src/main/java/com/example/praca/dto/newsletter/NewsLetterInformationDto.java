package com.example.praca.dto.newsletter;

import com.example.praca.dto.event.InformationEventDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
@Builder
public class NewsLetterInformationDto {
    private EventInformationDto eventInformationDto;
    private List<InformationEventDto> informationEventDtoList;
    private List<UserInformationDto> userInformationDtoList;

}
