package com.practice.lms.student.controller;

import com.practice.lms.student.dto.StudentRequestDto;
import com.practice.lms.student.dto.StudentResponseDto;
import com.practice.lms.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201 Created по гайдлайну
    public StudentResponseDto create(@RequestBody @Valid StudentRequestDto request) {
        return studentService.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getById(@PathVariable UUID id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<StudentResponseDto> getAll() {
        return studentService.getAll();
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(
            @PathVariable UUID id,
            @RequestBody @Valid StudentRequestDto request
    ) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content по гайдлайну
    public void delete(@PathVariable UUID id) {
        studentService.delete(id);
    }
}