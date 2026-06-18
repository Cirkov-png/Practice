package com.practice.lms.controller;


import com.practice.lms.dto.student.StudentRequestDto;
import com.practice.lms.dto.student.StudentResponseDto;
import com.practice.lms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public StudentResponseDto create(@RequestBody @Valid StudentRequestDto request) {
        return studentService.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<StudentResponseDto> getAll() {
        return studentService.getAll();
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid StudentRequestDto request
    ) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}