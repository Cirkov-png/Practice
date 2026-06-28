package com.practice.lms.testutil;

import com.practice.lms.common.model.Email;
import com.practice.lms.common.model.Money;
import com.practice.lms.course.model.Course;
import com.practice.lms.course.model.CourseSettings;
import com.practice.lms.enrollment.model.Enrollment;
import com.practice.lms.student.model.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Student createStudent(BigDecimal coins) {
        final var student = new Student();
        student.setId(UUID.randomUUID());
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail(new Email("john.doe@example.com"));
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setCoins(new Money(coins));
        return student;
    }

    public static Course createCourse(BigDecimal price) {
        final var course = new Course();
        course.setId(UUID.randomUUID());
        course.setTitle("Spring Basics");
        course.setDescription("Learn Spring Boot");
        course.setPrice(new Money(price));
        course.setCoinsPaid(Money.zero());
        return course;
    }

    public static CourseSettings createSettings(Course course, LocalDateTime startDate) {
        final var settings = new CourseSettings();
        settings.setId(UUID.randomUUID());
        settings.setStartDate(startDate);
        settings.setEndDate(startDate.plusMonths(1));
        settings.setIsPublic(true);
        settings.setCourse(course);
        return settings;
    }

    public static Enrollment createEnrollment(Student student, Course course) {
        final var enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setCoinsPaid(course.getPrice());
        return enrollment;
    }
}
