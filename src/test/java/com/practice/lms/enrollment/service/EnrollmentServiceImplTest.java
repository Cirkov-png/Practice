package com.practice.lms.enrollment.service;

import com.practice.lms.enrollment.exception.EnrollmentException;
import com.practice.lms.student.exception.StudentException;
import com.practice.lms.common.model.Money;
import com.practice.lms.course.service.CourseService;
import com.practice.lms.enrollment.mapper.EnrollmentMapper;
import com.practice.lms.enrollment.model.Enrollment;
import com.practice.lms.enrollment.repository.EnrollmentRepository;
import com.practice.lms.student.service.StudentService;
import com.practice.lms.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private StudentService studentService;

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentRepository repository;

    @Mock
    private EnrollmentMapper mapper;

    @InjectMocks
    private EnrollmentServiceImpl service;

    @Test
    void enrollStudent_WhenAlreadyEnrolled_ShouldThrowException() {
        // given
        final var studentId = UUID.randomUUID();
        final var courseId = UUID.randomUUID();
        when(repository.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(true);

        // when / then
        assertThrows(EnrollmentException.AlreadyEnrolled.class,
                () -> service.enrollStudent(studentId, courseId));
        verify(studentService, never()).findByIdInternalWithLock(any());
    }

    @Test
    void enrollStudent_WhenInsufficientFunds_ShouldThrowException() {
        // given
        final var student = TestDataFactory.createStudent(new BigDecimal("10"));
        final var course = TestDataFactory.createCourse(new BigDecimal("100"));

        when(repository.existsByStudentIdAndCourseId(student.getId(), course.getId())).thenReturn(false);
        when(studentService.findByIdInternalWithLock(student.getId())).thenReturn(student);
        when(courseService.findByIdInternalWithLock(course.getId())).thenReturn(course);

        // when / then
        assertThrows(StudentException.InsufficientFunds.class,
                () -> service.enrollStudent(student.getId(), course.getId()));
        verify(repository, never()).save(any());
    }

    @Test
    void enrollStudent_WhenValidRequest_ShouldDeductCoinsAndSaveEnrollment() {
        // given
        final var student = TestDataFactory.createStudent(new BigDecimal("200"));
        final var course = TestDataFactory.createCourse(new BigDecimal("100"));
        final var savedEnrollment = TestDataFactory.createEnrollment(student, course);

        when(repository.existsByStudentIdAndCourseId(student.getId(), course.getId())).thenReturn(false);
        when(studentService.findByIdInternalWithLock(student.getId())).thenReturn(student);
        when(courseService.findByIdInternalWithLock(course.getId())).thenReturn(course);
        when(repository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

        // when
        service.enrollStudent(student.getId(), course.getId());

        // then
        assertEquals(new BigDecimal("100"), student.getCoins().amount());
        assertEquals(new BigDecimal("100"), course.getCoinsPaid().amount());

        final var captor = ArgumentCaptor.forClass(Enrollment.class);
        verify(repository).save(captor.capture());
        assertEquals(student, captor.getValue().getStudent());
        assertEquals(course, captor.getValue().getCourse());
        assertEquals(new Money(new BigDecimal("100")), captor.getValue().getCoinsPaid());
    }

    @Test
    void enrollStudent_WhenExactBalance_ShouldSucceed() {
        // given
        final var student = TestDataFactory.createStudent(new BigDecimal("100"));
        final var course = TestDataFactory.createCourse(new BigDecimal("100"));
        final var savedEnrollment = TestDataFactory.createEnrollment(student, course);

        when(repository.existsByStudentIdAndCourseId(student.getId(), course.getId())).thenReturn(false);
        when(studentService.findByIdInternalWithLock(student.getId())).thenReturn(student);
        when(courseService.findByIdInternalWithLock(course.getId())).thenReturn(course);
        when(repository.save(any())).thenReturn(savedEnrollment);

        // when / then
        assertDoesNotThrow(() -> service.enrollStudent(student.getId(), course.getId()));
        assertEquals(BigDecimal.ZERO, student.getCoins().amount());
    }
}
