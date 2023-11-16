package com.example.praca.service;

import com.example.praca.dto.eventtype.EventTypeInformationDto;
import com.example.praca.exception.EventTypeNotFoundException;
import com.example.praca.model.EventType;
import com.example.praca.repository.EventTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@AllArgsConstructor
@Service
public class EventTypeService {
        private final EventTypeRepository EVENT_TYPE_REPOSITORY;

        public ReturnService getAllEventTypesInformation() {
            List<EventTypeInformationDto> eventTypeInformationDtos =  EVENT_TYPE_REPOSITORY.findAllEvents().orElseThrow(EventTypeNotFoundException::new)
                    .stream()
                    .map(x -> EventTypeInformationDto.of(x))
                    .collect(Collectors.toList());

            return ReturnService.returnInformation("", eventTypeInformationDtos, 1);
        }

    public EventType findById(Long id) {
        return EVENT_TYPE_REPOSITORY.findById(id).orElseThrow(EventTypeNotFoundException::new);
    }



}
