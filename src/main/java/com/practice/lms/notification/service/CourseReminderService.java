package com.practice.lms.notification.service;

import com.practice.lms.course.service.CourseService;
import com.practice.lms.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseReminderService {

    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final EmailNotificationService emailNotificationService;

    public void sendRemindersForCoursesStartingTomorrow() {
        final var tomorrow = LocalDate.now().plusDays(1);
        final var start = tomorrow.atStartOfDay();
        final var end = tomorrow.plusDays(1).atStartOfDay();

        final var courses = courseService.findCoursesStartingBetween(start, end);
        log.info("Found courses starting tomorrow: {}", courses.size());

        if (courses.isEmpty()) {
            return;
        }

        final var courseIds = courses.stream().map(course -> course.getId()).toList();
        final var enrollmentsByCourseId = enrollmentRepository.findAllByCourseIdsWithStudent(courseIds)
                .stream()
                .collect(java.util.stream.Collectors.groupingBy(e -> e.getCourse().getId()));

        for (final var course : courses) {
            final var enrollments = enrollmentsByCourseId.getOrDefault(course.getId(), List.of());
            log.info("Sending reminders for course: {}, enrolled students: {}", course.getTitle(), enrollments.size());

            for (final var enrollment : enrollments) {
                final var student = enrollment.getStudent();
                emailNotificationService.sendCourseReminder(
                        student.getEmail().value(),
                        student.getFirstName(),
                        course.getTitle(),
                        course.getSettings().getStartDate()
                );
            }
        }
    }
}
