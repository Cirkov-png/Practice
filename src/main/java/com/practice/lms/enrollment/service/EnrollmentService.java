package com.practice.lms.enrollment.service;

import com.practice.lms.enrollment.dto.EnrollmentResponseDto;

import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponseDto enrollStudent(UUID studentId, UUID courseId);
}
