package com.practice.lms.dto.lesson;

import lombok.Data;

@Data
public class LessonResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long courseId;
}