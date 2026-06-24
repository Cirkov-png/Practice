package com.practice.lms.course.mapper;

import com.practice.lms.course.dto.LessonRequestDto;
import com.practice.lms.course.dto.LessonResponseDto;
import com.practice.lms.course.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toEntity(LessonRequestDto request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponseDto toDto(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntityFromDto(LessonRequestDto request, @MappingTarget Lesson lesson);
}