package com.example.praca.repository;

import com.example.praca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniel Lezniak
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);

    @Query("select u from User u where :hobbyId in (select h.id from Hobby h )")
    Page<User> findAllByHobby(@Param("hobbyId") Long hobbyId, Pageable pageable);

    @Query("select u from User u where :hobbyId in (select h.id from Hobby h) and u.newsletter = :newsletter")
    Page<User> findAllByHobbyAndNewsletter(@Param("hobbyId") Long hobbyId, @Param("newsletter") Boolean newsletter, Pageable pageable);

    @Query("select u.phoneNumber from User u where u.email = :email")
    String findUserPhoneNumberById(@Param("email") String email);
    @Query("select u from User u where u.email LIKE :emailDomain%")
    List<User> findUserByEmailDomain(@Param("emailDomain") String emailDomain);

    List<User> findByEmailEndingWith(String emailDomain);

}
