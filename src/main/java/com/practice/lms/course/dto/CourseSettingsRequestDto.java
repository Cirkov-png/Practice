package com.practice.lms.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CourseSettingsRequestDto(
        @NotNull(message = "Start date is required")
        @Future(message = "Start date must be in the future")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        LocalDateTime endDate,

        @NotNull(message = "Visibility flag is required")
        Boolean isPublic
) {}