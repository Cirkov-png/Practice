package com.practice.lms.controller;

import com.practice.lms.dto.lesson.LessonRequestDto;
import com.practice.lms.dto.lesson.LessonResponseDto;
import com.practice.lms.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public LessonResponseDto create(@RequestBody @Valid LessonRequestDto request) {
        return lessonService.create(request);
    }

    @GetMapping("/{id}")
    public LessonResponseDto getById(@PathVariable Long id) {
        return lessonService.getById(id);
    }

    @GetMapping
    public List<LessonResponseDto> getAll() {
        return lessonService.getAll();
    }

    @PutMapping("/{id}")
    public LessonResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid LessonRequestDto request
    ) {
        return lessonService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        lessonService.delete(id);
    }
}