package com.example.praca.repository;

import com.example.praca.dto.task.TaskInformationDto;
import com.example.praca.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Daniel Lezniak
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.member.id = :userId and t.organization.id = :organizationId")
    Page<Task> findAllByUserIdAndOrganizationId(@Param("userId") Long userId, @Param("organizationId") Long organizationId, Pageable pageable);

    @Query("select t from Task  t where t.organization.id = :organizationId")
    Page<Task> findAllByOrganizationId(@Param("organizationId") Long organizationId, Pageable pageable);

    @Query("select t from Task t LEFT JOIN OrganizationMember om ON t.member.id = om.id WHERE om.userId = :userId")
    Page<Task> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select t from Task t where t.member.userId = :userId")
    Page<Task> findAllByMemberId(@Param("userId") Long userId, Pageable pageable);

    @Query("select t from Task t where t.member.userId = :userId and t.organization.event.id = :eventId")
    Page<Task> findAllByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId, Pageable pageable);
}
