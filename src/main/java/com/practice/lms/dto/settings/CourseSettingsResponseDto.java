package com.practice.lms.dto.settings;

import lombok.Data;

@Data
public class CourseSettingsResponseDto {
    private Long id;
    private String difficulty;
    private Integer duration;
    private Boolean isPublic;
    private Long courseId;
}