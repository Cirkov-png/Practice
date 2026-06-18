package com.practice.lms.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequestDto {

    @NotBlank(message = "Lesson title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Course id is required")
    private Long courseId;
}