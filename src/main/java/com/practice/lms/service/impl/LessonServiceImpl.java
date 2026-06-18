package com.practice.lms.service.impl;


import com.practice.lms.dto.lesson.LessonRequestDto;
import com.practice.lms.dto.lesson.LessonResponseDto;
import com.practice.lms.entity.Course;
import com.practice.lms.entity.Lesson;
import com.practice.lms.exception.ResourceNotFoundException;
import com.practice.lms.mapper.LessonMapper;
import com.practice.lms.repository.CourseRepository;
import com.practice.lms.repository.LessonRepository;
import com.practice.lms.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    final private LessonRepository repository;
    final private LessonMapper mapper;
    private final CourseRepository courseRepository;

    @Override
    public LessonResponseDto create(LessonRequestDto request){

        Lesson lesson = mapper.toEntity(request);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + request.getCourseId()));

        lesson.setCourse(course);

        return mapper.toDto(repository.save(lesson));

    }

    @Override
    public  LessonResponseDto getById(Long id){

        Optional<Lesson> optionalLesson = repository.findById(id);
        if(optionalLesson.isEmpty()){
            throw new ResourceNotFoundException("Lesson not found with id " + id);
        }

        Lesson lesson = optionalLesson.get();
        return mapper.toDto(lesson);
    }

    @Override
    public LessonResponseDto update(Long id, LessonRequestDto request) {

        Optional<Lesson> optionalLesson = repository.findById(id);
        if(optionalLesson.isEmpty()){
            throw new ResourceNotFoundException("Lesson not found with id " + id);
        }
        Lesson lesson = optionalLesson.get();

        lesson.setContent(request.getContent());
        lesson.setTitle(request.getTitle());


        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + request.getCourseId()));
        lesson.setCourse(course);

        return mapper.toDto(repository.save(lesson));

    }

    @Override
    public List<LessonResponseDto> getAll() {
        List<LessonResponseDto> finalDtoList =
                repository.findAll().stream().map(lesson -> mapper.toDto(lesson)).toList();

        return finalDtoList;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
