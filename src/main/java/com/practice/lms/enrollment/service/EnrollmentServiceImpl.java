package com.practice.lms.enrollment.service;

import com.practice.lms.common.exception.EnrollmentException;
import com.practice.lms.common.exception.StudentException;
import com.practice.lms.course.service.CourseService;
import com.practice.lms.enrollment.dto.EnrollmentResponseDto;
import com.practice.lms.enrollment.mapper.EnrollmentMapper;
import com.practice.lms.enrollment.model.Enrollment;
import com.practice.lms.enrollment.repository.EnrollmentRepository;
import com.practice.lms.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentRepository repository;
    private final EnrollmentMapper mapper;

    @Override
    @Transactional
    public EnrollmentResponseDto enrollStudent(final UUID studentId, final UUID courseId) {
        log.info("Processing enrollment. Student: {}, Course: {}", studentId, courseId);

        if (repository.existsByStudentIdAndCourseId(studentId, courseId)) {
            log.warn("Enrollment failed: already enrolled. Student ID: {}, Course ID: {}", studentId, courseId);
            throw new EnrollmentException.AlreadyEnrolled(studentId, courseId);
        }

        final var student = studentService.findByIdInternalWithLock(studentId);
        final var course = courseService.findByIdInternalWithLock(courseId);

        final var price = course.getPrice();
        final var balance = student.getCoins();

        if (balance.isLessThan(price)) {
            log.warn("Enrollment failed: insufficient funds. Balance: {}, Price: {}", balance.amount(), price.amount());
            throw new StudentException.InsufficientFunds(studentId);
        }

        student.setCoins(balance.subtract(price));
        course.setCoinsPaid(course.getCoinsPaid().add(price));

        final var enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setCoinsPaid(price);

        final var savedEnrollment = repository.save(enrollment);
        log.info("Enrollment completed. Record ID: {}", savedEnrollment.getId());

        return mapper.toResponseDto(savedEnrollment);
    }
}
