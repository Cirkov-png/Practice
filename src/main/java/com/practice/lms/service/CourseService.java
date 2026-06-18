package com.practice.lms.service;

import com.practice.lms.dto.course.CourseRequestDto;
import com.practice.lms.dto.course.CourseResponseDto;

import java.util.List;

public interface CourseService {

    CourseResponseDto create(CourseRequestDto request);
    CourseResponseDto getById(Long id);
    List<CourseResponseDto> getAll();
    CourseResponseDto update(Long id, CourseRequestDto dto);

    void delete(Long id);

    void enrollStudent(Long courseId, Long studentId);
}