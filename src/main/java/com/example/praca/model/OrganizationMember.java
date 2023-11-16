package com.example.praca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Lezniak
 */
@Data
@Entity
@Table(name = "organization_member")
public class OrganizationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;
    @Column
    private Long userId;

    @ManyToMany(mappedBy = "organizationMembers", fetch = FetchType.EAGER)
    Set<Organization> organizations;

    public static OrganizationMember of(User user, Organization organization) {
        OrganizationMember organizationMember = new OrganizationMember();
        organizationMember.setName(user.getName());
        organizationMember.setPhoneNumber(user.getPhoneNumber());
        organizationMember.setOrganizations(Collections.singleton(organization));
        organizationMember.setUserId(user.getId());

        return organizationMember;
    }
}
