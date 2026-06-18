    package com.practice.lms.repository;

    import com.practice.lms.entity.Lesson;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface LessonRepository extends JpaRepository<Lesson, Long> {
    }