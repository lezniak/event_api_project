package com.example.praca.repository;

import com.example.praca.model.Hobby;
import com.example.praca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Daniel Lezniak
 */
@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
    void deleteAllByUsers(User user);

    @Query("select h from Hobby h")
    Optional<List<Hobby>> getAllHobbies();

    @Query("select h.id from Hobby h where h.name = :name")
    Optional<Long> getHobbyIdByName(@Param("name") String name);
}
