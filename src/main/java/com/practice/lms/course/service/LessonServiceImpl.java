package com.practice.lms.course.service;

import com.practice.lms.common.exception.CourseException;
import com.practice.lms.course.dto.LessonRequestDto;
import com.practice.lms.course.dto.LessonResponseDto;
import com.practice.lms.course.mapper.LessonMapper;
import com.practice.lms.course.repository.LessonRepository;
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
public class LessonServiceImpl implements LessonService {

    private final LessonRepository repository;
    private final CourseService courseService;
    private final LessonMapper mapper;

    @Override
    @Transactional
    public LessonResponseDto create(final LessonRequestDto request) {
        log.info("Creating lesson for course id: {}", request.courseId());
        final var course = courseService.findByIdInternal(request.courseId());

        final var lesson = mapper.toEntity(request);
        lesson.setCourse(course);

        return mapper.toDto(repository.save(lesson));
    }

    @Override
    public LessonResponseDto getById(final UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new CourseException.NotFound(id));
    }

    @Override
    public List<LessonResponseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponseDto update(final UUID id, final LessonRequestDto request) {
        log.info("Updating lesson with id: {}", id);
        final var lesson = repository.findById(id)
                .orElseThrow(() -> new CourseException.NotFound(id));

        final var course = courseService.findByIdInternal(request.courseId());

        mapper.updateEntityFromDto(request, lesson);
        lesson.setCourse(course);

        return mapper.toDto(repository.save(lesson));
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting lesson by id: {}", id);
        if (!repository.existsById(id)) {
            throw new CourseException.NotFound(id);
        }
        repository.deleteById(id);
    }
}