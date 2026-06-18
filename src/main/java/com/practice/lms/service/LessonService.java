package com.practice.lms.service;

import com.practice.lms.dto.lesson.LessonRequestDto;
import com.practice.lms.dto.lesson.LessonResponseDto;

import java.util.List;

public interface LessonService {

    LessonResponseDto create(LessonRequestDto request);

    LessonResponseDto getById(Long id);

    List<LessonResponseDto> getAll();

    LessonResponseDto update(Long id, LessonRequestDto request);

    void delete(Long id);
}
