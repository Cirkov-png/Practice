package com.practice.lms.dto.course;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private Integer price;
    private String description;
}