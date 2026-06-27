package com.practice.lms.course.service;

import com.practice.lms.course.dto.LessonRequestDto;
import com.practice.lms.course.dto.LessonResponseDto;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    LessonResponseDto create(LessonRequestDto request);

    LessonResponseDto getById(UUID id);

    List<LessonResponseDto> getAll();

    LessonResponseDto update(UUID id, LessonRequestDto request);

    void delete(UUID id);
}