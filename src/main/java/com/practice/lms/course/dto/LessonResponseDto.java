package com.practice.lms.course.dto;

import java.util.UUID;

public record LessonResponseDto(
        UUID id,
        String title,
        Integer duration,
        UUID courseId
) {}