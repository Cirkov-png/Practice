package com.practice.lms.course.controller;

import com.practice.lms.course.dto.LessonRequestDto;
import com.practice.lms.course.dto.LessonResponseDto;
import com.practice.lms.course.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponseDto create(
            @PathVariable final UUID courseId,
            @RequestBody @Valid final LessonRequestDto request
    ) {
        log.info("API: Creating lesson for course id: {}", courseId);
        // Protect the contract: ensure that the lesson is linked to the course from the URL
        final var scopedRequest = new LessonRequestDto(request.title(), request.duration(), courseId);
        return service.create(scopedRequest);
    }

    @GetMapping("/{id}")
    public LessonResponseDto getById(@PathVariable final UUID courseId, @PathVariable final UUID id) {
        log.info("API: Fetching lesson id: {}", id);
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public LessonResponseDto update(
            @PathVariable final UUID courseId,
            @PathVariable final UUID id,
            @RequestBody @Valid final LessonRequestDto request
    ) {
        log.info("API: Updating lesson id: {}", id);
        final var scopedRequest = new LessonRequestDto(request.title(), request.duration(), courseId);
        return service.update(id, scopedRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID courseId, @PathVariable final UUID id) {
        log.info("API: Deleting lesson id: {}", id);
        service.delete(id);
    }
}