package com.practice.lms.course.repository;

import com.practice.lms.course.model.CourseSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CourseSettingsRepository extends JpaRepository<CourseSettings, UUID> {
    Optional<CourseSettings> findByCourseId(UUID courseId);
}