package com.practice.lms.service;

import com.practice.lms.dto.student.StudentRequestDto;
import com.practice.lms.dto.student.StudentResponseDto;

import java.util.List;

public interface StudentService {

    StudentResponseDto create(StudentRequestDto request);
    StudentResponseDto getById(Long id);
    List<StudentResponseDto> getAll();
    StudentResponseDto update(Long id, StudentRequestDto request);
    void delete(Long id);
}
