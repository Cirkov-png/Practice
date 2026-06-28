package com.practice.lms.course.service;

import com.practice.lms.course.dto.CourseSettingsRequestDto;
import com.practice.lms.course.dto.CourseSettingsResponseDto;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsService {

    CourseSettingsResponseDto create(UUID courseId, CourseSettingsRequestDto request);

    List<CourseSettingsResponseDto> getAll();

    CourseSettingsResponseDto getByCourseId(UUID courseId);

    CourseSettingsResponseDto updateByCourseId(UUID courseId, CourseSettingsRequestDto request);

    void delete(UUID id);
}