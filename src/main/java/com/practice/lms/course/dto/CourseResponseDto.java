package com.practice.lms.course.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CourseResponseDto(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        BigDecimal coinsPaid
) {}