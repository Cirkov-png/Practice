package com.practice.lms.enrollment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record EnrollmentResponseDto(
        UUID id,
        UUID studentId,
        UUID courseId,
        BigDecimal coinsPaid
) {}
