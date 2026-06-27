package com.practice.lms.course.controller;

import com.practice.lms.course.dto.CourseSettingsRequestDto;
import com.practice.lms.course.dto.CourseSettingsResponseDto;
import com.practice.lms.course.service.CourseSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/settings")
@RequiredArgsConstructor
public class CourseSettingsController {

    private final CourseSettingsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseSettingsResponseDto create(
            @PathVariable final UUID courseId,
            @RequestBody @Valid final CourseSettingsRequestDto request
    ) {
        return service.create(courseId, request);
    }

    @GetMapping
    public CourseSettingsResponseDto getByCourseId(@PathVariable final UUID courseId) {
        return service.getByCourseId(courseId);
    }

    @PutMapping
    public CourseSettingsResponseDto updateByCourseId(
            @PathVariable final UUID courseId,
            @RequestBody @Valid final CourseSettingsRequestDto request
    ) {
        return service.updateByCourseId(courseId, request);
    }
}