package com.example.praca.dto.orgniazationmember;

import com.example.praca.model.Organization;
import com.example.praca.model.OrganizationMember;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Data
public class OrganizationMemberInformationDto {
    private String name;
    private Long id;
    private Map<String, Long> organizationName;
    private String phoneNumber;

    public static OrganizationMemberInformationDto of(OrganizationMember organizationMember) {
        OrganizationMemberInformationDto dto = new OrganizationMemberInformationDto();
        dto.setName(organizationMember.getName());
        dto.setId(organizationMember.getId());
        dto.setPhoneNumber(organizationMember.getPhoneNumber());
        dto.setOrganizationName(createOrganizationMap(organizationMember.getOrganizations()));

        return dto;
    }

    private static Map<String, Long> createOrganizationMap(Set<Organization> organizationSet) {
        Map<String, Long> map = new HashMap<>();
        organizationSet.stream()
                .map(x -> map.put(x.getName(), x.getId()))
                .collect(Collectors.toList());

        return map;
    }
}
