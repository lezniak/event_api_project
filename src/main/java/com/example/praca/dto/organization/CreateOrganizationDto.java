package com.example.praca.dto.organization;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class CreateOrganizationDto {
    private String name;
    private Long eventId;
}
