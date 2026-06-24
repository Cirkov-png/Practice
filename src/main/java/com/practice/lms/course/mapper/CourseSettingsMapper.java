package com.practice.lms.course.mapper;

import com.practice.lms.course.dto.CourseSettingsRequestDto;
import com.practice.lms.course.dto.CourseSettingsResponseDto;
import com.practice.lms.course.model.CourseSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseSettingsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    CourseSettings toEntity(CourseSettingsRequestDto request);

    @Mapping(target = "courseId", source = "course.id")
    CourseSettingsResponseDto toDto(CourseSettings courseSettings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntityFromDto(CourseSettingsRequestDto request, @MappingTarget CourseSettings courseSettings);
}