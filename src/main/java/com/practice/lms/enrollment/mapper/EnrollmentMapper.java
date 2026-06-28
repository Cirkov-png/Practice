package com.practice.lms.enrollment.mapper;

import com.practice.lms.enrollment.dto.EnrollmentResponseDto;
import com.practice.lms.enrollment.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "coinsPaid", source = "coinsPaid.amount")
    EnrollmentResponseDto toResponseDto(Enrollment enrollment);
}
