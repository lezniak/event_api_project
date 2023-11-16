package com.example.praca.dto.organization;

import com.example.praca.dto.orgniazationmember.OrganizationMemberInformationDto;
import com.example.praca.dto.task.TaskInformationDto;
import com.example.praca.model.Organization;
import com.example.praca.model.Task;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class OrganizationInformationDto {
    private Long id;
    private String name;
    private String ownerName;
    private Long ownerId;
    private String eventName;
    private Long eventId;
    private List<OrganizationMemberInformationDto> organizationMemberInformationDtoList;

    public static OrganizationInformationDto of(Organization organization) {
        OrganizationInformationDto dto = new OrganizationInformationDto();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setOwnerName(organization.getOwner().getName());
        dto.setOwnerId(organization.getOwner().getId());
        dto.setEventId(organization.getEvent().getId());
        dto.setEventName(organization.getEvent().getName());
        dto.setOrganizationMemberInformationDtoList(organization.getOrganizationMembers()
                .stream()
                .map(x -> OrganizationMemberInformationDto.of(x))
                .collect(Collectors.toList()));
        return dto;
    }
}
