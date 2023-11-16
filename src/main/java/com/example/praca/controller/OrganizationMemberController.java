package com.example.praca.controller;

import com.example.praca.dto.orgniazationmember.AddOrganizationMemberDto;
import com.example.praca.dto.orgniazationmember.AddOrganizationMemberEmailDto;
import com.example.praca.service.OrganizationMemberService;
import com.example.praca.service.ReturnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daniel Lezniak
 */
@RestController
@AllArgsConstructor
@RequestMapping("/organization-member")
@Slf4j
public class OrganizationMemberController {

    private final OrganizationMemberService ORGANIZATION_MEMBER_SERVICE;

    @PostMapping("")
    public ReturnService addOrganizationMember(@RequestBody AddOrganizationMemberDto dto) {
        return ORGANIZATION_MEMBER_SERVICE.addOrganizationMember(dto);
    }

    @DeleteMapping("")
    public ReturnService deleteOrganizationMember(@RequestParam Long organizationId, @RequestParam Long memberId) {
        return ORGANIZATION_MEMBER_SERVICE.deleteMember(organizationId, memberId);
    }

    @PostMapping("/email")
    public ReturnService addOrganizationMemberByEmail(@RequestBody AddOrganizationMemberEmailDto dto) {
        return ORGANIZATION_MEMBER_SERVICE.addOrganizationMemberByEmail(dto);
    }
}
