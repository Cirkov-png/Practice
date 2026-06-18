package com.practice.lms.repository;

import com.practice.lms.entity.Course;
import com.practice.lms.entity.CourseSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseSettingsRepository extends JpaRepository<CourseSettings, Long> {
    Optional<CourseSettings> findByCourseId(Long courseId);
}
