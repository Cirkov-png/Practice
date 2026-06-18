package com.practice.lms.dto.settings;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseSettingsRequestDto {

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duration;

    @NotNull(message = "Visibility is required")
    private Boolean isPublic;

    @NotNull(message = "Course id is required")
    private Long courseId;
}