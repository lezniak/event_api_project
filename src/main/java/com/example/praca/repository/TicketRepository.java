package com.example.praca.repository;

import com.example.praca.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select t from Ticket t where t.content = :content")
    Optional<Ticket> findTicketByContent(@Param("content") byte[] content);

    @Query("select t from Ticket t where t.user.id = :userId and t.used = false")
    Page<Ticket> findTicketByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("select t from Ticket t where t.user.id = :userId and t.used = true ")
    Page<Ticket> findTicketByUserIdUsed(@Param("userId") Long userId, Pageable pageable);

    @Query("select t from Ticket t where t.user.id = :userId and t.event.id = :eventId and t.used = false")
    Page<Ticket> findTicketByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId, Pageable pageable);
    @Query("select t from Ticket t where t.user.id = :userId and t.event.id = :eventId and t.used = true ")
    Page<Ticket> findTicketByUserIdAndEventIdUsed(@Param("userId") Long userId, @Param("eventId") Long eventId, Pageable pageable);

    @Query("select t from Ticket t where t.event.id = :eventId and t.used = false")
    Page<Ticket> findTicketByEventId(@Param("eventId") Long eventId, Pageable pageable);
    @Query("select t from Ticket t where t.event.id = :eventId and t.used = true")
    Page<Ticket> findTicketByEventIdUsed(@Param("eventId") Long eventId, Pageable pageable);
}
