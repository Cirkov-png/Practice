package com.practice.lms.controller;

import com.practice.lms.dto.settings.CourseSettingsRequestDto;
import com.practice.lms.dto.settings.CourseSettingsResponseDto;
import com.practice.lms.service.CourseSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-settings")
@RequiredArgsConstructor
public class CourseSettingsController {

    private final CourseSettingsService courseSettingsService;

    @GetMapping("/course/{courseId}")
    public CourseSettingsResponseDto getByCourseId(
            @PathVariable Long courseId
    ) {
        return courseSettingsService.getByCourseId(courseId);
    }

    @PutMapping("/course/{courseId}")
    public CourseSettingsResponseDto updateByCourseId(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseSettingsRequestDto request
    ) {
        return courseSettingsService.updateByCourseId(courseId, request);
    }
}