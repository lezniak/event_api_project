package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.EventPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface EventPaymentRepository extends JpaRepository<EventPayment, Long> {
    @Query("select e from EventPayment e where e.userPayment.id = :userId and e.eventPayment.id = :eventId and e.success = true")
    Optional<EventPayment> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
    @Query("select e from EventPayment e where e.paymentToken = :token")
    Optional<EventPayment> findByToken(@Param("token") String token);
}
