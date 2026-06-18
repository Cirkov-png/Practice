package com.practice.lms.mapper;


import com.practice.lms.dto.student.StudentRequestDto;
import com.practice.lms.dto.student.StudentResponseDto;
import com.practice.lms.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity(StudentRequestDto request);
    StudentResponseDto toDto(Student student);
}
