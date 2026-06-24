package com.practice.lms.student.service;

import com.practice.lms.student.dto.StudentRequestDto;
import com.practice.lms.student.dto.StudentResponseDto;
import com.practice.lms.student.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponseDto create(StudentRequestDto request);
    StudentResponseDto getById(UUID id);
    List<StudentResponseDto> getAll();
    StudentResponseDto update(UUID id, StudentRequestDto request);
    void delete(UUID id);
    Student findByIdInternalWithLock(UUID id);
}
