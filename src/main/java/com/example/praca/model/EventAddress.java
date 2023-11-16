package com.example.praca.model;

import com.example.praca.dto.eventaddress.EventAddressDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author Daniel Lezniak
 */

@Data
@Entity
@Table(name = "event_address")
public class EventAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition= "Decimal(8,6)", nullable = false)
    private Double lat;

    @Column(columnDefinition = "Decimal(9,6)", nullable = false)
    private Double lng;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "event_address")
    private Event event;

    public static EventAddress of(EventAddressDto dto) {
        EventAddress eventAddress = new EventAddress();

        eventAddress.setAddress(dto.getAddress());
        eventAddress.setCity(dto.getCity());
        eventAddress.setLat(dto.getLat());
        eventAddress.setLng(dto.getLng());

        return eventAddress;
    }

    public static EventAddress updateEventAddress(EventAddress eventAddress, EventAddressDto dto) {
        eventAddress.setAddress(dto.getAddress());
        eventAddress.setLng(dto.getLng());
        eventAddress.setLat(dto.getLat());
        eventAddress.setCity(dto.getCity());

        return eventAddress;
    }


    @Override
    public String toString() {
        return "EventAddress{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

}
