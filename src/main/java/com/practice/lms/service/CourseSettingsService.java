package com.practice.lms.service;

import com.practice.lms.dto.settings.CourseSettingsRequestDto;
import com.practice.lms.dto.settings.CourseSettingsResponseDto;

import java.util.List;

public interface CourseSettingsService {


    CourseSettingsResponseDto create(CourseSettingsRequestDto request);

    List<CourseSettingsResponseDto> getAll();

    CourseSettingsResponseDto getByCourseId(Long courseId);

    CourseSettingsResponseDto updateByCourseId(Long courseId, CourseSettingsRequestDto request);

    void delete(Long id);
}
