package com.example.praca.dto.event;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateEventDto {
    CreateEventDto createEventDto;
    Long eventId;
}
