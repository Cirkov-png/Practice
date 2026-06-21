package com.practice.lms.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LessonRequestDto(
        @NotBlank(message = "Lesson title cannot be empty")
        String title,

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1 minute")
        Integer duration,

        @NotNull(message = "Course ID is required")
        UUID courseId
) {}