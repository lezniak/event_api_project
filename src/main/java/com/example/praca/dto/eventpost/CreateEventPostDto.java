package com.example.praca.dto.eventpost;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class CreateEventPostDto {
    private Long eventId;
    private String content;
}
