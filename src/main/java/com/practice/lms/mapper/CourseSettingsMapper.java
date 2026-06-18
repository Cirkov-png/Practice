package com.practice.lms.mapper;


import com.practice.lms.dto.settings.CourseSettingsRequestDto;
import com.practice.lms.dto.settings.CourseSettingsResponseDto;
import com.practice.lms.entity.CourseSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseSettingsMapper {

    @Mapping(target = "course", ignore = true)
    CourseSettings toEntity(CourseSettingsRequestDto request);

    @Mapping(target = "courseId", source = "course.id")
    CourseSettingsResponseDto toDto(CourseSettings courseSettings);
}
