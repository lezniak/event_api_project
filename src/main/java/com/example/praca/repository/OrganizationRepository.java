package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("select o from Organization o where o.event.id = :eventId and o.name = :organizationName")
    Optional<Organization> findOrganizationByEventIdAndOrganizationName(@Param("eventId") Long eventId, @Param("organizationName") String organizationName);

    @Query("select o from Organization o where o.owner.id = :ownerId")
    Page<Organization> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("select o from Organization o where o.owner.id = :ownerId and o.event.id = :eventId")
    Page<Organization> findByOwnerIdAndEventId(@Param("ownerId") Long ownerId, @Param("eventId") Long eventId, Pageable pageable);
}
