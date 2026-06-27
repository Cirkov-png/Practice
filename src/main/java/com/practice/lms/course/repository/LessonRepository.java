package com.practice.lms.course.repository;

import com.practice.lms.course.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findByCourseId(UUID courseId);
}