package com.practice.lms.course.controller;

import com.practice.lms.course.dto.LessonRequestDto;
import com.practice.lms.course.dto.LessonResponseDto;
import com.practice.lms.course.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

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
        final var scopedRequest = new LessonRequestDto(request.title(), request.duration(), courseId);
        return service.create(scopedRequest);
    }

    @GetMapping("/{id}")
    public LessonResponseDto getById(@PathVariable final UUID courseId, @PathVariable final UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public LessonResponseDto update(
            @PathVariable final UUID courseId,
            @PathVariable final UUID id,
            @RequestBody @Valid final LessonRequestDto request
    ) {
        final var scopedRequest = new LessonRequestDto(request.title(), request.duration(), courseId);
        return service.update(id, scopedRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID courseId, @PathVariable final UUID id) {
        service.delete(id);
    }
}