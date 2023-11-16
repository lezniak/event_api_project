package com.example.praca.repository;

import com.example.praca.model.Event;
import com.example.praca.model.EventAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
@Repository
public interface EventAddressRepository extends JpaRepository<EventAddress, Long> {

    Optional<EventAddress> findAllByEvent(Event event);
}
