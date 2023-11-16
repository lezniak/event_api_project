package com.example.praca.service;

import com.example.praca.dto.eventaddress.EventAddressDto;
import com.example.praca.exception.EventAddressNotFoundException;
import com.example.praca.model.Event;
import com.example.praca.model.EventAddress;
import com.example.praca.repository.EventAddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniel Lezniak
 */
@Service
@AllArgsConstructor
public class EventAddressService {
    private final EventAddressRepository EVENT_ADDRESS_REPOSITORY;

    public ReturnService updateEventAddress(Event event, EventAddressDto eventAddressDto) {
        try {
            EventAddress eventAddressDb = findByEvent(event);
            EventAddress dto = EventAddress.updateEventAddress(eventAddressDb, eventAddressDto);
            EventAddress eventAddressUpdated = EVENT_ADDRESS_REPOSITORY.save(dto);
            return ReturnService.returnInformation("Succ. update event address",eventAddressUpdated, 1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. update event address: " + ex.getMessage(), -1);
        }
    }

    protected EventAddress findByEvent(Event event) {
        return EVENT_ADDRESS_REPOSITORY.findAllByEvent(event).orElseThrow(() -> new EventAddressNotFoundException());
    }
}
