package com.practice.lms.mapper;


import com.practice.lms.dto.lesson.LessonRequestDto;
import com.practice.lms.dto.lesson.LessonResponseDto;
import com.practice.lms.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "course", ignore = true)
    Lesson toEntity(LessonRequestDto request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponseDto toDto(Lesson lesson);
}
