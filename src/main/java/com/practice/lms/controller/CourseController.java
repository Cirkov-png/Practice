package com.practice.lms.controller;

import com.practice.lms.dto.course.CourseRequestDto;
import com.practice.lms.dto.course.CourseResponseDto;
import com.practice.lms.entity.Course;
import com.practice.lms.entity.Student;
import com.practice.lms.exception.ResourceNotFoundException;
import com.practice.lms.repository.CourseRepository;
import com.practice.lms.repository.StudentRepository;
import com.practice.lms.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;


    @PostMapping
    public CourseResponseDto create(@RequestBody @Valid CourseRequestDto request) {
        return courseService.create(request);
    }

    @GetMapping("/{id}")
    public CourseResponseDto getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @GetMapping
    public List<CourseResponseDto> getAll() {
        return courseService.getAll();
    }

    @PutMapping("/{id}")
    public CourseResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid CourseRequestDto request
    ) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @PostMapping("/{courseId}/students/{studentId}")
    public void enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.enrollStudent(courseId, studentId);
    }
}