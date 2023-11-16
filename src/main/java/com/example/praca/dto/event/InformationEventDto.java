package com.example.praca.dto.event;

import com.example.praca.dto.eventaddress.EventAddressInformation;
import com.example.praca.dto.eventpost.EventPostInformationDto;
import com.example.praca.model.Event;
import com.example.praca.model.EventAddress;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class InformationEventDto <T>{
    private Long id;
    private String name;
    private String ownerName;
    private String startDate;
    private String eventDescription;
    private Long ownerId;
    private EventAddressInformation eventAddressInformation;
    private boolean forAll;
    private boolean isMember;
    private List<EventPostInformationDto> eventPostInformationDtoList;
    private int maxMembers;

    public static InformationEventDto of(Event event, EventAddress eventAddress, boolean isMember) {
        InformationEventDto dto = new InformationEventDto();
        dto.setName(event.getName());
        dto.setId(event.getId());
        dto.setOwnerName(event.getUser().getName());
        dto.setEventAddressInformation(EventAddressInformation.of(eventAddress));
        dto.setStartDate(event.getStartDate().toString());
        dto.setOwnerId(event.getUser().getId());
        dto.setEventDescription(event.getEventDescription());
        dto.setForAll(event.isForAll());
        dto.setMaxMembers(event.getMaxMembers());
        dto.isMember = isMember;
        if (event.getPosts() != null) {
            dto.setEventPostInformationDtoList(event.getPosts()
                    .stream()
                    .map(x -> EventPostInformationDto.of(x))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static InformationEventDto newsletterDto(Event event, EventAddress eventAddress, boolean isMember) {
        InformationEventDto dto = new InformationEventDto();
        dto.setName(event.getName());
        dto.setId(event.getId());
        dto.setOwnerName(event.getUser().getName());
        dto.setEventAddressInformation(EventAddressInformation.of(eventAddress));
        dto.setStartDate(event.getStartDate().toString());
        dto.setOwnerId(event.getUser().getId());
        dto.setEventDescription(event.getEventDescription());
        dto.setForAll(event.isForAll());
        dto.isMember = isMember;

        return dto;
    }

}
