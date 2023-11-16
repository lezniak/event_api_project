package com.example.praca.dto.eventaddress;

import com.example.praca.model.EventAddress;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventAddressDto {
    private String city;
    private String address;
    private Double lng;
    private Double lat;

    public static EventAddressDto of(EventAddress dto) {
        EventAddressDto eventAddressDto = new EventAddressDto();
        eventAddressDto.setAddress(dto.getAddress());
        eventAddressDto.setCity(dto.getCity());
        eventAddressDto.setLng(dto.getLng());
        eventAddressDto.setLat(dto.getLat());

        return eventAddressDto;
    }
}
