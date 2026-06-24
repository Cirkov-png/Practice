package com.practice.lms.enrollment.repository;

import com.practice.lms.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.course.id = :courseId")
    List<Enrollment> findAllByCourseIdWithStudent(@Param("courseId") UUID courseId);
}
