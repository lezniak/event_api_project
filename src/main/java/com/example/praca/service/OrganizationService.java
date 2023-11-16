package com.example.praca.service;

import com.example.praca.dto.EventOrganizationFilter;

import com.example.praca.dto.common.PageableDto;
import com.example.praca.dto.organization.*;
import com.example.praca.dto.orgniazationmember.OrganizationMemberInformationDto;
import com.example.praca.exception.InvalidEventOwnerException;
import com.example.praca.exception.InvalidOrganizationOwnerException;
import com.example.praca.exception.OrganizationAlreadyExistException;
import com.example.praca.exception.OrganizationNotFoundException;
import com.example.praca.model.*;
import com.example.praca.repository.CompanyRepository;
import com.example.praca.repository.OrganizationMemberRepository;
import com.example.praca.repository.OrganizationRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniel Lezniak
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrganizationService {
    private final OrganizationRepository ORGANIZATION_REPOSITORY;
    private final UserService USER_SERVICE;
    private final EventService EVENT_SERVICE;

    private final OrganizationMemberRepository ORGANIZATION_MEMBER_REPOSITORY;
    private final CompanyRepository COMPANY_REPOSITORY;

    public ReturnService createOrganization(CreateOrganizationDto dto) {
        Event event = EVENT_SERVICE.findEventById(dto.getEventId());
        if (!EVENT_SERVICE.isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();
        if (organizationExistInEvent(dto.getEventId(), dto.getName()))
            throw new OrganizationAlreadyExistException();
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
        try {
            Organization organization = ORGANIZATION_REPOSITORY.save(Organization.of(dto, event, user));
            return ReturnService.returnInformation("Succ. create organization", OrganizationInformationDto.of(organization), 1);
        } catch (Exception ex) {
            log.error("Err. create organization email: "  + user.getEmail() + "eventId: " + dto.getEventId() + "Err: " + ex.getMessage());
            return ReturnService.returnError("Err. create organization: " + ex.getMessage(), -1);
        }
    }

    public ReturnService updateOrganization(UpdateOrganizationDto dto) {

        Organization organization = getOrganizationById(dto.getOrganizationId());
        if (EVENT_SERVICE.isEventOwner(organization.getEvent().getId()))
            throw new InvalidEventOwnerException();

        if (isOrganizationOwner(dto.getOrganizationId()))
            throw new InvalidOrganizationOwnerException();
        try {
            Organization updatedOrganization = ORGANIZATION_REPOSITORY.save(Organization.updateOrganization(organization, dto));
            return ReturnService.returnInformation("succ. update organization", OrganizationInformationDto.of(updatedOrganization),1);
        } catch (Exception ex) {
            log.error("Err. update organization id: "  + dto.getOrganizationId()  + "Err: " + ex.getMessage());
            return ReturnService.returnError("Err. create organization: " + ex.getMessage(), -1);
        }
    }

    public ReturnService deleteOrganization(Long eventId, Long organizationId) {
        if (EVENT_SERVICE.isEventOwner(eventId))
            throw new InvalidEventOwnerException();

        if (isOrganizationOwner(organizationId))
            throw new InvalidOrganizationOwnerException();

        Organization organization = getOrganizationById(organizationId);
        try {
            ORGANIZATION_REPOSITORY.deleteById(organizationId);
            return ReturnService.returnInformation("succ. delete organization", 1);
        } catch (Exception ex) {
            log.error("Err. delete organization id: "  + organizationId  + "Err: " + ex.getMessage());
            return ReturnService.returnError("Err. create organization: " + ex.getMessage(), -1);
        }
    }

    public Organization getOrganizationById(Long organizationId) {
        return ORGANIZATION_REPOSITORY.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    }

    public boolean organizationExistInEvent(Long eventId, String organizationName) {
        return ORGANIZATION_REPOSITORY.findOrganizationByEventIdAndOrganizationName(eventId, organizationName).isPresent();
    }

    public boolean isOrganizationOwner(Long organizationOwner) {
        String token = JwtTokenService.getJwtokenFromHeader();
        Claims claims = new DefaultClaims();

        if (StringUtils.isNotBlank(token) && !token.isEmpty())
            claims = JwtTokenService.getClaimsFromJwtoken(token);

        User user = USER_SERVICE.findUserByEmail(claims.get("sub").toString());

        return organizationOwner == user.getId();
    }

    public ReturnService getAllOrganization(int pageNo, int pageSize, String sortBy, String sortDir, EventOrganizationFilter filterParam, String... value) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Organization> organizationPage = null;

        Long userId = USER_SERVICE.getUserIdByEmail(USER_SERVICE.getUserEmailFromJwt());
        switch (filterParam) {
            case A:
                organizationPage = ORGANIZATION_REPOSITORY.findByOwnerId(userId, pageable);
                break;
            case E:
                Long eventId = Long.valueOf(value[0]);
                organizationPage = ORGANIZATION_REPOSITORY.findByOwnerIdAndEventId(userId, eventId, pageable);
                break;
        }

        if (organizationPage == null)
            throw new OrganizationNotFoundException();
        List<Organization> organizationList = organizationPage.getContent();
        List<OrganizationInformationDto> organizationInformationDtos = organizationList.stream()
                .map(x -> OrganizationInformationDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", PageableDto.of(organizationInformationDtos, organizationPage), 1);
    }

    public void addOrganizationMember(Organization organization, OrganizationMember organizationMember) {
        try {

            organization.getOrganizationMembers().add(organizationMember);
            organizationMember.getOrganizations().add(organization);

            ORGANIZATION_REPOSITORY.saveAndFlush(organization);
            ORGANIZATION_MEMBER_REPOSITORY.saveAndFlush(organizationMember);
        } catch (Exception ex) {
            log.error("Err. add organization member; "  + ex.getStackTrace());
        }
    }

    public ReturnService getAllCompany() {
        List<Company> companyList = COMPANY_REPOSITORY.findAll();

        List<CompanyInformationDto> companyInformationDtos = companyList.stream()
                .map(x -> CompanyInformationDto.of(x))
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", companyInformationDtos, 1);
    }

    public ReturnService addCompanyToOrganization(AddCompanyDto dto) {
        Event event = EVENT_SERVICE.findEventById(dto.getEventId());
        if (EVENT_SERVICE.isEventOwner(event.getUser().getId()))
            throw new InvalidEventOwnerException();
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());
        Company company = COMPANY_REPOSITORY.findById(dto.getCompanyId()).orElseThrow(() -> new NotFoundException("Company not found"));

        try {
            Organization updatedOrganization = ORGANIZATION_REPOSITORY.save(Organization.of(company, event, user));
            return ReturnService.returnInformation("succ. update organization", OrganizationInformationDto.of(updatedOrganization),1);
        } catch (Exception ex) {
            return ReturnService.returnError("Err. create organization: " + ex.getMessage(), -1);
        }
    }

    public ReturnService userInOrganization(UserInOrganizationDto dto) {
        User user = USER_SERVICE.findUserByEmail(USER_SERVICE.getUserEmailFromJwt());

        Organization organization = getOrganizationById(dto.getOrganizationId());

        List<OrganizationMember> organizationMembers = organization.getOrganizationMembers()
                .stream()
                .filter(organizationMember -> organizationMember.getUserId() == user.getId())
                .collect(Collectors.toList());

        return ReturnService.returnInformation("", !organizationMembers.isEmpty(), 1);

    }


}
