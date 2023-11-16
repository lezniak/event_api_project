package com.example.praca.repository;

import com.example.praca.model.UserLastLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface UserLastLocationRepository extends JpaRepository<UserLastLogin, Long> {

    @Query("select l from UserLastLogin l where l.user.id = :userId")
    Optional<UserLastLogin> findByUserId(@Param("userId") Long userId);
}
