package com.practice.lms.course.service;

import com.practice.lms.common.exception.CourseException;
import com.practice.lms.course.dto.CourseSettingsRequestDto;
import com.practice.lms.course.dto.CourseSettingsResponseDto;
import com.practice.lms.course.mapper.CourseSettingsMapper;
import com.practice.lms.course.model.CourseSettings;
import com.practice.lms.course.repository.CourseSettingsRepository;
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
public class CourseSettingsServiceImpl implements CourseSettingsService {

    private final CourseSettingsRepository repository;
    private final CourseService courseService; // Изоляция репозиториев соблюдена!
    private final CourseSettingsMapper mapper;

    @Override
    @Transactional
    public CourseSettingsResponseDto create(final UUID courseId, final CourseSettingsRequestDto request) {
        log.info("Creating settings for course id: {}", courseId);
        final var course = courseService.findByIdInternal(courseId);

        final var settings = mapper.toEntity(request);
        settings.setCourse(course);

        return mapper.toDto(repository.save(settings));
    }

    @Override
    public List<CourseSettingsResponseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CourseSettingsResponseDto getByCourseId(final UUID courseId) {
        return repository.findByCourseId(courseId)
                .map(mapper::toDto)
                .orElseThrow(() -> new CourseException.SettingsNotFound(courseId));
    }

    @Override
    @Transactional
    public CourseSettingsResponseDto updateByCourseId(final UUID courseId, final CourseSettingsRequestDto request) {
        log.info("Updating settings for course id: {}", courseId);
        final var course = courseService.findByIdInternal(courseId);

        final var settings = repository.findByCourseId(courseId)
                .orElseGet(CourseSettings::new);

        mapper.updateEntityFromDto(request, settings);
        settings.setCourse(course);

        return mapper.toDto(repository.save(settings));
    }

    @Override
    @Transactional
    public void delete(final UUID id) {
        log.info("Deleting settings by id: {}", id);
        if (!repository.existsById(id)) {
            throw new CourseException.SettingsNotFound(id); // Используем контекстный эксепшен
        }
        repository.deleteById(id);
    }
}