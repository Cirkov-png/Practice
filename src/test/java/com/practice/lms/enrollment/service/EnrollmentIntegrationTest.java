package com.practice.lms.enrollment.service;

import com.practice.lms.common.exception.EnrollmentException;
import com.practice.lms.common.exception.StudentException;
import com.practice.lms.common.model.Email;
import com.practice.lms.common.model.Money;
import com.practice.lms.course.model.Course;
import com.practice.lms.course.repository.CourseRepository;
import com.practice.lms.enrollment.repository.EnrollmentRepository;
import com.practice.lms.student.model.Student;
import com.practice.lms.student.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EnrollmentIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @AfterEach
    void cleanup() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void enrollStudent_WhenValidRequest_ShouldDeductCoinsAndPersist() {
        // given
        final var student = studentRepository.save(buildStudent("alice@example.com", new BigDecimal("500")));
        final var course = courseRepository.save(buildCourse(new BigDecimal("200")));

        // when
        final var result = enrollmentService.enrollStudent(student.getId(), course.getId());

        // then
        assertNotNull(result.id());
        assertEquals(student.getId(), result.studentId());
        assertEquals(course.getId(), result.courseId());
        assertEquals(new BigDecimal("200.0000"), result.coinsPaid());

        final var updatedStudent = studentRepository.findById(student.getId()).orElseThrow();
        assertEquals(new BigDecimal("300.0000"), updatedStudent.getCoins().amount());

        final var updatedCourse = courseRepository.findById(course.getId()).orElseThrow();
        assertEquals(new BigDecimal("200.0000"), updatedCourse.getCoinsPaid().amount());
    }

    @Test
    void enrollStudent_WhenAlreadyEnrolled_ShouldThrowException() {
        // given
        final var student = studentRepository.save(buildStudent("bob@example.com", new BigDecimal("500")));
        final var course = courseRepository.save(buildCourse(new BigDecimal("100")));
        enrollmentService.enrollStudent(student.getId(), course.getId());

        // when / then
        assertThrows(EnrollmentException.AlreadyEnrolled.class,
                () -> enrollmentService.enrollStudent(student.getId(), course.getId()));
    }

    @Test
    void enrollStudent_WhenInsufficientFunds_ShouldThrowAndNotPersist() {
        // given
        final var student = studentRepository.save(buildStudent("carol@example.com", new BigDecimal("50")));
        final var course = courseRepository.save(buildCourse(new BigDecimal("200")));

        // when / then
        assertThrows(StudentException.InsufficientFunds.class,
                () -> enrollmentService.enrollStudent(student.getId(), course.getId()));
        assertEquals(0, enrollmentRepository.findAll().size());
    }

    private Student buildStudent(String email, BigDecimal coins) {
        final var student = new Student();
        student.setFirstName("Test");
        student.setLastName("User");
        student.setEmail(new Email(email));
        student.setDateOfBirth(LocalDate.of(2000, 1, 1));
        student.setCoins(new Money(coins));
        return student;
    }

    private Course buildCourse(BigDecimal price) {
        final var course = new Course();
        course.setTitle("Test Course");
        course.setPrice(new Money(price));
        course.setCoinsPaid(Money.zero());
        return course;
    }
}
