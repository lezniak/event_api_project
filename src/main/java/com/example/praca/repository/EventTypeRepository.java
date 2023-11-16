package com.example.praca.repository;

import com.example.praca.model.EventType;
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
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    @Query("select t from EventType t where t.name = :name")
    Optional<EventType> findByName(@Param("name") String name);

    @Query("select t from EventType t")
    Optional<List<EventType>> findAllEvents();
}
