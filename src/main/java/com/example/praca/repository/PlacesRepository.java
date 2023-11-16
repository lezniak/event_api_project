package com.example.praca.repository;

import com.example.praca.model.Places;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Daniel Lezniak
 */
public interface PlacesRepository extends JpaRepository<Places, Long> {
    @Procedure("GET_PLACES_BY_RANGE")
    List<Places> findAllInRange(Double userLat, Double userLng, Double range);

    @Query("select p from Places p where p.capacity >= :capacity")
    Page<Places> findAllByCapacity(@Param("capacity")int capacity, Pageable pageable);
}
