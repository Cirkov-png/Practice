package com.practice.lms.course.controller;

import com.practice.lms.course.dto.CourseSettingsRequestDto;
import com.practice.lms.course.dto.CourseSettingsResponseDto;
import com.practice.lms.course.service.CourseSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
        log.info("API: Creating settings for course id: {}", courseId);
        return service.create(courseId, request);
    }

    @GetMapping
    public CourseSettingsResponseDto getByCourseId(@PathVariable final UUID courseId) {
        log.info("API: Fetching settings for course id: {}", courseId);
        return service.getByCourseId(courseId);
    }

    @PutMapping
    public CourseSettingsResponseDto updateByCourseId(
            @PathVariable final UUID courseId,
            @RequestBody @Valid final CourseSettingsRequestDto request
    ) {
        log.info("API: Updating settings for course id: {}", courseId);
        return service.updateByCourseId(courseId, request);
    }
}