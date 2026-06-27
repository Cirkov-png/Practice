package com.practice.lms.course.controller;

import com.practice.lms.course.dto.CourseRequestDto;
import com.practice.lms.course.dto.CourseResponseDto;
import com.practice.lms.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponseDto create(@RequestBody @Valid final CourseRequestDto request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public CourseResponseDto getById(@PathVariable final UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CourseResponseDto> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public CourseResponseDto update(
            @PathVariable final UUID id,
            @RequestBody @Valid final CourseRequestDto request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID id) {
        service.delete(id);
    }
}