package com.example.praca.dto.organization;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class UpdateOrganizationDto {
    private Long organizationId;
    private String name;
}
