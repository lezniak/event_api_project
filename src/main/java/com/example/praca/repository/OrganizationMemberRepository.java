package com.example.praca.repository;

import com.example.praca.model.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Daniel Lezniak
 */
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
}
