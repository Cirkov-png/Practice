package com.practice.lms.notification.service;

import com.practice.lms.course.service.CourseService;
import com.practice.lms.enrollment.repository.EnrollmentRepository;
import com.practice.lms.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseReminderServiceTest {

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private CourseReminderService reminderService;

    @Test
    void sendRemindersForCoursesStartingTomorrow_WhenNoCoursesTomorrow_ShouldNotSendEmails() {
        // given
        when(courseService.findCoursesStartingBetween(any(), any())).thenReturn(List.of());

        // when
        reminderService.sendRemindersForCoursesStartingTomorrow();

        // then
        verify(emailNotificationService, never()).sendCourseReminder(any(), any(), any(), any());
    }

    @Test
    void sendRemindersForCoursesStartingTomorrow_WhenCourseHasEnrolledStudents_ShouldSendEmailForEach() {
        // given
        final var course = TestDataFactory.createCourse(new BigDecimal("50"));
        final var settings = TestDataFactory.createSettings(course, LocalDateTime.now().plusDays(1));
        course.setSettings(settings);

        final var student1 = TestDataFactory.createStudent(BigDecimal.ZERO);
        final var student2 = TestDataFactory.createStudent(BigDecimal.ZERO);
        final var enrollment1 = TestDataFactory.createEnrollment(student1, course);
        final var enrollment2 = TestDataFactory.createEnrollment(student2, course);

        when(courseService.findCoursesStartingBetween(any(), any())).thenReturn(List.of(course));
        when(enrollmentRepository.findAllByCourseIdsWithStudent(List.of(course.getId())))
                .thenReturn(List.of(enrollment1, enrollment2));

        // when
        reminderService.sendRemindersForCoursesStartingTomorrow();

        // then
        verify(emailNotificationService, times(2)).sendCourseReminder(any(), any(), any(), any());
    }

    @Test
    void sendRemindersForCoursesStartingTomorrow_WhenCourseHasNoEnrollments_ShouldNotSendEmails() {
        // given
        final var course = TestDataFactory.createCourse(new BigDecimal("50"));
        final var settings = TestDataFactory.createSettings(course, LocalDateTime.now().plusDays(1));
        course.setSettings(settings);

        when(courseService.findCoursesStartingBetween(any(), any())).thenReturn(List.of(course));
        when(enrollmentRepository.findAllByCourseIdsWithStudent(List.of(course.getId()))).thenReturn(List.of());

        // when
        reminderService.sendRemindersForCoursesStartingTomorrow();

        // then
        verify(emailNotificationService, never()).sendCourseReminder(any(), any(), any(), any());
    }
}
