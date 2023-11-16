package com.example.praca.dto.eventaddress;

import com.example.praca.model.EventAddress;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class EventAddressInformation {
    private String city;
    private String address;
    private Double lng;
    private Double lat;


    public static EventAddressInformation of(EventAddress eventAddress) {
        EventAddressInformation eventAddressInformation = new EventAddressInformation();

        eventAddressInformation.setAddress(eventAddress.getAddress());
        eventAddressInformation.setCity(eventAddress.getCity());
        eventAddressInformation.setLat(eventAddress.getLat());
        eventAddressInformation.setLng(eventAddress.getLng());

        return eventAddressInformation;
    }
}
