package com.practice.lms.course.service;

import com.practice.lms.common.exception.CourseException;
import com.practice.lms.course.dto.CourseRequestDto;
import com.practice.lms.course.dto.CourseResponseDto;
import com.practice.lms.course.mapper.CourseMapper;
import com.practice.lms.course.model.Course;
import com.practice.lms.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    @Override
    @Transactional
    public CourseResponseDto create(final CourseRequestDto request) {
        log.info("Creating new course with title: {}", request.title());
        final var course = mapper.toEntity(request);
        return mapper.toDto(repository.save(course));
    }

    @Override
    public CourseResponseDto getById(final UUID id) {
        return mapper.toDto(findByIdInternal(id));
    }

    @Override
    public Course findByIdInternal(final UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CourseException.NotFound(id));
    }

    @Override
    public List<CourseResponseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponseDto update(final UUID id, final CourseRequestDto request) {
        log.info("Updating course with id: {}", id);
        final var course = findByIdInternal(id);

        mapper.updateEntityFromDto(request, course);
        return mapper.toDto(repository.save(course));
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting course by id: {}", id);
        if (!repository.existsById(id)) {
            throw new CourseException.NotFound(id);
        }
        repository.deleteById(id);
    }

    @Override
    public Course findByIdInternalWithLock(final UUID id) {
        return repository.findByIdWithLock(id)
                .orElseThrow(() -> new CourseException.NotFound(id));
    }

    @Override
    public List<Course> findCoursesStartingBetween(final LocalDateTime start, final LocalDateTime end) {
        return repository.findAllStartingBetween(start, end);
    }
}