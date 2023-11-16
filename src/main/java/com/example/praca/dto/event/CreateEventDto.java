package com.example.praca.dto.event;

import com.example.praca.dto.eventaddress.EventAddressDto;
import com.example.praca.model.EventAddress;
import com.example.praca.model.EventType;
import com.example.praca.model.User;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Daniel Lezniak
 */
@Data
public class CreateEventDto {
    Long ownerId;
    String name;
    String eventDescription;
    String startDate;
    Timestamp startDateD;
    EventAddressDto eventAddress;
    Integer maxMembers;
    Long eventType;
    EventType eventTypeM;
    EventAddress eventAddressM;
    User owner;
    boolean genarateQrCode;
    boolean forAll;

}
