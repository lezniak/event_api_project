package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.EventType;
import com.example.praca.model.User;
import com.google.api.client.util.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Daniel Lezniak
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
    @Query("select e from Event e join e.event_address a")
    Page<Event> findAllEvents(Pageable pageable);
    @Query("select e from Event e join e.event_address a where a.city = :city ")
    Page<Event> findAllByCity(@Param("city") String city, Pageable pageable);
    @Query("select e from Event  e where e.startDate >= :dateStart and e.startDate <= :dateEnd")
    Page<Event> findAllByDate(@Param("dateStart") Timestamp dateStart, @Param("dateEnd") Timestamp dateEnd, Pageable pageable);

    @Query("select e from Event e where e.user = :owner")
    Page<Event> findAllByOwner(@Param("owner") User user, Pageable pageable);

    @Query("select e from Event e where e.startDate >= :startDate and e.startDate <= :endDate")
    Page<Event> findAllByDateRante(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate, Pageable pageable);

//    @Query("select e from Event  e where e.event_types in :types")
    @Query("select e from Event e join e.eventType as et where et.id in :types")
    Page<Event> findAllByType(@Param("types") List<Long> types, Pageable pageable);

    @Query("select e from Event  e where e.id in :eventId")
    Page<Event> findAllById(@Param("eventId") List<Long> eventId, Pageable pageable);

    @Query("select e from Event e where e.startDate >= :dateStart and e.startDate <= :dateEnd and e.eventType.id = :eventTypeId")
    Page<Event> findAllByEventTypeAndDateRange(@Param("eventTypeId") Long eventTypeId, @Param("dateStart") Timestamp dateStart, @Param("dateEnd") Timestamp dateEnd, Pageable pageable);

    @Procedure("GET_EVENTS_BY_RANGE")
    List<Event> findAllInRange(@Param("userLat") Double userLat, @Param("userLng") Double userLng,@Param("range") Double range);

    @Query("select e.user.id from Event e where e.id = :eventId")
    Long getEventOwnerByEventId(@Param("eventId") Long eventId);

    @Query("select e from Event e where e.startDate >= :dateStart and e.startDate <= :dateEnd and e.eventType.id = :eventTypeId")
    Page<Event> findAllByEventTypeAndDateRangeScheduler(@Param("eventTypeId") Long eventTypeId, @Param("dateStart") Timestamp dateStart, @Param("dateEnd") Timestamp dateEnd, Pageable pageable);


}
