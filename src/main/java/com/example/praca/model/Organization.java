package com.example.praca.model;

import com.example.praca.dto.organization.CreateOrganizationDto;
import com.example.praca.dto.organization.UpdateOrganizationDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private User owner;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "organization_member_associate",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    List<OrganizationMember> organizationMembers;

    public static Organization of(CreateOrganizationDto dto, Event event, User user) {
        Organization organization = new Organization();
        organization.setName(dto.getName());
        organization.setEvent(event);
        organization.setOwner(user);
        List<OrganizationMember> organizationMembers1 = new ArrayList<OrganizationMember>();
        organization.setOrganizationMembers(organizationMembers1);

        return organization;
    }

    public static Organization updateOrganization(Organization organization, UpdateOrganizationDto dto) {
        organization.setName(dto.getName());
        return organization;
    }

    public static Organization of (Company company, Event event, User user) {
        Organization organization = new Organization();
        organization.setName(company.getName());
        organization.setEvent(event);
        organization.setOwner(user);
        List<OrganizationMember> organizationMembers1 = new ArrayList<OrganizationMember>();
        organization.setOrganizationMembers(organizationMembers1);

        return organization;
    }

}
