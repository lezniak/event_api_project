package com.example.praca.dto.places;

import com.example.praca.model.Places;
import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class PlaceInformationDto {
    private String name;
    private Double lng;
    private Double lat;
    private String email;
    private String phoneNumber;
    private String city;
    private String address;
    private int capacity;

    public static PlaceInformationDto of (Places places) {
        PlaceInformationDto dto = new PlaceInformationDto();

        dto.setName(places.getName());
        dto.setLat(places.getLat());
        dto.setLng(places.getLng());
        dto.setEmail(places.getEmail());
        dto.setPhoneNumber(places.getPhoneNumber());
        dto.setCity(places.getCity());
        dto.setAddress(places.getAddress());
        dto.setCapacity(places.getCapacity());

        return dto;
    }
}
