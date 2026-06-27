package com.practice.lms.student.service;

import com.practice.lms.student.exception.StudentException;
import com.practice.lms.common.model.Money;
import com.practice.lms.student.dto.StudentRequestDto;
import com.practice.lms.student.dto.StudentResponseDto;
import com.practice.lms.student.mapper.StudentMapper;
import com.practice.lms.student.model.Student;
import com.practice.lms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    public Student findByIdInternalWithLock(final UUID id) {
        return repository.findByIdWithLock(id)
                .orElseThrow(() -> new StudentException.NotFound(id));
    }

    @Override
    @Transactional
    public StudentResponseDto create(final StudentRequestDto request) {
        final var student = mapper.toEntity(request);
        student.setCoins(Money.zero());
        return mapper.toDto(repository.save(student));
    }

    @Override
    public StudentResponseDto getById(final UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new StudentException.NotFound(id));
    }

    @Override
    public List<StudentResponseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponseDto update(final UUID id, final StudentRequestDto request) {
        final var student = repository.findById(id)
                .orElseThrow(() -> new StudentException.NotFound(id));
        mapper.updateEntityFromDto(request, student);
        return mapper.toDto(repository.save(student));
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        if (!repository.existsById(id)) {
            throw new StudentException.NotFound(id);
        }
        repository.deleteById(id);
    }
}