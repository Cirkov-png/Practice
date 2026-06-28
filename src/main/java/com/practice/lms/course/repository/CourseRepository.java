package com.practice.lms.course.repository;

import com.practice.lms.course.model.Course;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @EntityGraph(attributePaths = {"settings"})
    @Query("SELECT c FROM Course c")
    List<Course> findAllWithSettings();

    @EntityGraph(attributePaths = {"settings"})
    Optional<Course> findWithSettingsById(UUID id);

    @Query("SELECT c FROM Course c JOIN FETCH c.settings s WHERE s.startDate >= :start AND s.startDate < :end")
    List<Course> findAllStartingBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findByIdWithLock(@Param("id") UUID id);
}
