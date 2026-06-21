package com.practice.lms.student.service;

import com.practice.lms.common.exception.StudentException;
import com.practice.lms.common.model.Money;
import com.practice.lms.student.dto.StudentRequestDto;
import com.practice.lms.student.dto.StudentResponseDto;
import com.practice.lms.student.mapper.StudentMapper;
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
    @Transactional
    public StudentResponseDto create(final StudentRequestDto request) {
        log.info("Creating new student with email: {}", request.email());

        final var student = mapper.toEntity(request);
        student.setCoins(Money.zero()); // Защита от Primitive Obsession через Value Object

        final var savedStudent = repository.save(student);
        return mapper.toDto(savedStudent);
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
        log.info("Updating student with id: {}", id);

        final var student = repository.findById(id)
                .orElseThrow(() -> new StudentException.NotFound(id));

        mapper.updateEntityFromDto(request, student);
        return mapper.toDto(repository.save(student));
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting student by id: {}", id);

        if (!repository.existsById(id)) {
            throw new StudentException.NotFound(id);
        }
        repository.deleteById(id);
    }
}