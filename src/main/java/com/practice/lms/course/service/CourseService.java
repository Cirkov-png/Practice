package com.practice.lms.course.service;

import com.practice.lms.course.dto.CourseRequestDto;
import com.practice.lms.course.dto.CourseResponseDto;
import com.practice.lms.course.model.Course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponseDto create(CourseRequestDto request);
    CourseResponseDto getById(UUID id);
    List<CourseResponseDto> getAll();
    CourseResponseDto update(UUID id, CourseRequestDto dto);
    void delete(UUID id);
    Course findByIdInternal(UUID id);
    Course findByIdInternalWithLock(UUID id);
    List<Course> findCoursesStartingBetween(LocalDateTime start, LocalDateTime end);
}
