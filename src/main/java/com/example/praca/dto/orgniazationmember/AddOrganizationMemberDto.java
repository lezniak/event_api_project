package com.example.praca.dto.orgniazationmember;

import lombok.Data;

/**
 * @author Daniel Lezniak
 */
@Data
public class AddOrganizationMemberDto {
    private Long memberId;
    private Long organizationId;
    private Long eventId;
}
