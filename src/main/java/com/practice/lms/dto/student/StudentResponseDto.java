package com.practice.lms.dto.student;

import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String name;
    private String email;
    private Integer coins;
}