package com.example.praca.service;

import com.example.praca.dto.orgniazationmember.AddOrganizationMemberDto;
import com.example.praca.dto.orgniazationmember.AddOrganizationMemberEmailDto;
import com.example.praca.dto.orgniazationmember.OrganizationMemberInformationDto;
import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.exception.InvalidOrganizationOwnerException;
import com.example.praca.exception.OrganizationNotFoundException;
import com.example.praca.exception.UserNotFoundException;
import com.example.praca.model.Event;
import com.example.praca.model.Organization;
import com.example.praca.model.OrganizationMember;
import com.example.praca.model.User;
import com.example.praca.repository.OrganizationMemberRepository;
import com.example.praca.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrganizationMemberService {
    private final UserService USER_SERVICE;
    private final OrganizationMemberRepository ORGANIZATION_MEMBER_REPOSITORY;
    private final OrganizationService ORGANIZATION_SERVICE;
    private final EventService EVENT_SERVICE;
    private final OrganizationRepository ORGANIZATION_REPOSITORY;

    public ReturnService addOrganizationMember(AddOrganizationMemberDto dto) {
        Organization organization = ORGANIZATION_SERVICE.getOrganizationById(dto.getOrganizationId());
        if (!EVENT_SERVICE.isEventOwner(organization.getEvent().getId()))
            throw new InvalidEventOwnerException();

        if (!ORGANIZATION_SERVICE.isOrganizationOwner(dto.getOrganizationId()))
            throw new InvalidOrganizationOwnerException();
        User user = USER_SERVICE.findUserById(dto.getMemberId());
        Event event = EVENT_SERVICE.findEventById(dto.getEventId());

        try {
            OrganizationMember member = ORGANIZATION_MEMBER_REPOSITORY.save(OrganizationMember.of(user, organization));
            ORGANIZATION_SERVICE.addOrganizationMember(organization, member);
            return ReturnService.returnInformation("Succ. add organization memebr", OrganizationMemberInformationDto.of(member), 1);
        } catch (Exception ex) {
            log.error("Err. add organization member; "  + ex.getMessage());
            return ReturnService.returnError("Err. add organization memeber: " + ex.getMessage(), -1);
        }
    }

    public ReturnService deleteMember(Long organizationId, Long memberId) {
        Organization organization = ORGANIZATION_SERVICE.getOrganizationById(organizationId);
        if (EVENT_SERVICE.isEventOwner(organization.getEvent().getId()))
            throw new InvalidEventOwnerException();

        if (ORGANIZATION_SERVICE.isOrganizationOwner(organizationId))
            throw new InvalidOrganizationOwnerException();
        OrganizationMember member = organization.getOrganizationMembers().stream()
                .filter(x -> x.getId() == memberId)
                .findAny().get();
        try {
            organization.getOrganizationMembers().remove(member);
            ORGANIZATION_REPOSITORY.save(organization);
            return ReturnService.returnInformation("Succ. remove member", 1);
        } catch (Exception ex) {
            log.error("Err. delete organization member; "  + ex.getMessage());
            return ReturnService.returnError("Err. delete organization memeber: " + ex.getMessage(), -1);
        }
    }

    public OrganizationMember getOrganizationMemberById(Long organizationMemberId) {
        return ORGANIZATION_MEMBER_REPOSITORY.findById(organizationMemberId).orElseThrow(() -> new OrganizationNotFoundException());
    }

    public ReturnService addOrganizationMemberByEmail(AddOrganizationMemberEmailDto dto) {
        Organization organization = ORGANIZATION_SERVICE.getOrganizationById(dto.getOrganizationId());
        if (!EVENT_SERVICE.isEventOwner(organization.getEvent().getUser().getId()))
            throw new InvalidEventOwnerException();

        if (!ORGANIZATION_SERVICE.isOrganizationOwner(dto.getOrganizationId()))
            throw new InvalidOrganizationOwnerException();

        if (dto.getEmail().startsWith("*")) {
            List<User> usersWithEmail = USER_SERVICE.findAllUsersByEmailDomain(dto.getEmail().substring(1));
            if (usersWithEmail.isEmpty())
                throw new UserNotFoundException();

            try {
                for (User user : usersWithEmail) {
                    OrganizationMember organizationMember  = ORGANIZATION_MEMBER_REPOSITORY.save(OrganizationMember.of(user, organization));
                    ORGANIZATION_SERVICE.addOrganizationMember(organization, organizationMember);
                }
                return ReturnService.returnInformation("Succ. add organization memebers", 1);
            } catch (Exception ex) {
                log.error("Err. add organization member; "  + ex.getMessage());
                return ReturnService.returnError("Err. add organization memeber: " + ex.getMessage(), -1);
            }
        } else {
            User user = USER_SERVICE.findUserByEmail(dto.getEmail());
            try {
                OrganizationMember member = ORGANIZATION_MEMBER_REPOSITORY.save(OrganizationMember.of(user, organization));
                ORGANIZATION_SERVICE.addOrganizationMember(organization, member);
                return ReturnService.returnInformation("Succ. add organization memebr", OrganizationMemberInformationDto.of(member), 1);
            } catch (Exception ex) {
                log.error("Err. add organization member; "  + ex.getMessage());
                return ReturnService.returnError("Err. add organization memeber: " + ex.getMessage(), -1);
            }
        }
    }
}
