package com.practice.lms.service.impl;


import com.practice.lms.dto.settings.CourseSettingsRequestDto;
import com.practice.lms.dto.settings.CourseSettingsResponseDto;
import com.practice.lms.entity.Course;
import com.practice.lms.entity.CourseSettings;
import com.practice.lms.exception.ResourceNotFoundException;
import com.practice.lms.mapper.CourseMapper;
import com.practice.lms.mapper.CourseSettingsMapper;
import com.practice.lms.repository.CourseRepository;
import com.practice.lms.repository.CourseSettingsRepository;
import com.practice.lms.service.CourseSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseSettingsServiceImpl implements CourseSettingsService {

    public final CourseSettingsMapper mapper;
    private final CourseSettingsRepository repository;
    private final CourseRepository courseRepository;

    @Override
    public CourseSettingsResponseDto create(CourseSettingsRequestDto request) {

        CourseSettings courseSettings = mapper.toEntity(request);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + request.getCourseId()));


        courseSettings.setCourse(course);

        return mapper.toDto(repository.save(courseSettings));

    }

    @Override
    public List<CourseSettingsResponseDto> getAll() {
        List<CourseSettingsResponseDto> finalDtoResponce =
                repository.findAll().stream().map(courseSettings -> mapper.toDto(courseSettings)).toList();
        return finalDtoResponce;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public CourseSettingsResponseDto updateByCourseId(Long courseId, CourseSettingsRequestDto request) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }


        Optional<CourseSettings> optionalSettings = repository.findByCourseId(courseId);
        CourseSettings courseSettings;

        if (optionalSettings.isPresent()) {

            courseSettings = optionalSettings.get();
        } else {

            courseSettings = new CourseSettings();
            courseSettings.setCourse(courseRepository.getReferenceById(courseId));
        }

        courseSettings.setDifficulty(request.getDifficulty());
        courseSettings.setDuration(request.getDuration());
        courseSettings.setIsPublic(request.getIsPublic());

        return mapper.toDto(repository.save(courseSettings));
    }

    @Override
    public CourseSettingsResponseDto getByCourseId(Long courseId) {
        Optional<CourseSettings> optionalCourseSettings = repository.findByCourseId(courseId);
        if (optionalCourseSettings.isEmpty()){
            throw new ResourceNotFoundException("Settings not found with id " + courseId);
        }
        CourseSettings courseSettings = optionalCourseSettings.get();
        return mapper.toDto(courseSettings);
    }
}
