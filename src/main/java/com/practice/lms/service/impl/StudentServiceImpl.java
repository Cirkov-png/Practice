package com.practice.lms.service.impl;

import com.practice.lms.dto.student.*;
import com.practice.lms.entity.Student;
import com.practice.lms.exception.ResourceNotFoundException;
import com.practice.lms.mapper.StudentMapper;
import com.practice.lms.repository.StudentRepository;
import com.practice.lms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    public StudentResponseDto create(StudentRequestDto request) {
        Student student = mapper.toEntity(request);
        return mapper.toDto(repository.save(student));
    }

    @Override
    public StudentResponseDto getById(Long id) {
        Optional<Student> optionalStudent = repository.findById(id);
        if(optionalStudent.isEmpty()){
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        Student student = optionalStudent.get();
        return mapper.toDto(student);
    }

    @Override
    public List<StudentResponseDto> getAll() {
        List<StudentResponseDto> finalDtoList
                = repository.findAll().stream().map(student -> mapper.toDto(student)).toList();
        return finalDtoList;
    }

    @Override
    public StudentResponseDto update(Long id, StudentRequestDto request) {
        Optional<Student> optionalStudent = repository.findById(id);
        if(optionalStudent.isEmpty()){
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        Student student = optionalStudent.get();
        student.setName(request.getName());
        student.setEmail(request.getEmail());

        return mapper.toDto(repository.save(student));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}