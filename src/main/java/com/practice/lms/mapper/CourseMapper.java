package com.practice.lms.mapper;

import com.practice.lms.dto.course.CourseRequestDto;
import com.practice.lms.dto.course.CourseResponseDto;
import com.practice.lms.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toEntity(CourseRequestDto request);
    CourseResponseDto toDto(Course course);
}
