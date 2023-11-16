package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.EventPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface EventPostRepository extends JpaRepository<EventPost, Long> {

    @Query("select e from EventPost  e where e.event.id = :eventId")
    Page<EventPost> findALlByEvent(@Param("eventId") Long eventId, Pageable pageable);

    @Query("select e from EventPost e where e.user.id = :creatorId")
    Page<EventPost> findAllByCreator(@Param("creatorId") Long creatorId, Pageable pageable);

    @Query("select e from EventPost  e where e.id = :id")
    Optional<EventPost> findById(@Param("id") Long id);

}
