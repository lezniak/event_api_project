package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.EventMembers;
import com.example.praca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
@Repository
public interface EventMembersRepository extends JpaRepository<EventMembers, Long> {
    @Query("select e from EventMembers e where e.user_id = :userId and e.event_id = :eventId")
    Optional<EventMembers> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select e from EventMembers e where e.user_id = :userId")
    List<EventMembers> findAllByUserId(@Param("userId") Long userId);

    @Query("select e from EventMembers e where e.event_id = :eventId")
    Page<EventMembers> findEventMembersByEventId(@Param("eventId") Long eventId, Pageable pageable);
    @Query("select e from EventMembers e where e.event_id = :eventId and e.accepted = false")
    Page<EventMembers> findEventMembersByEventIdToAccepted(@Param("eventId") Long eventId, Pageable pageable);

    @Query("select e from EventMembers e where e.event_id = :eventId and e.accepted = true and e.user_id = :userId")
    Optional<EventMembers> findEventMembersByEventIdAndUserId(@Param("eventId") Long eventId,@Param("userId") Long userId);

    @Query("delete from EventMembers e where e.user_id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("delete from EventMembers e where e.event_id = :eventId")
    void deleteByEventId(@Param("eventId") Long eventId);

    @Query("select em.event_id from EventMembers em where em.user_id = :userId and em.event_id IN (select e.id from Event e where e.history = true)")
    List<Long> getHistoryEventByUserId(@Param("userId") Long userId);

    @Query("select em from EventMembers em where em.accepted = true and em.event_id = :eventId")
    Page<EventMembers> getAllAccepted(@Param("eventId") Long eventId, Pageable pageable);

}
