package com.example.praca.dto.orgniazationmember;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class AddOrganizationMemberEmailDto {
    private String email;
    private Long organizationId;
    private Long eventId;
}
