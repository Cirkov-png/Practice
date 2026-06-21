package com.practice.lms.student.mapper;

import com.practice.lms.common.model.Email;
import com.practice.lms.common.model.Money;
import com.practice.lms.student.dto.StudentRequestDto;
import com.practice.lms.student.dto.StudentResponseDto;
import com.practice.lms.student.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coins", ignore = true) // Задается в сервисе (например, 0 при регистрации)
    @Mapping(target = "email", source = "email") // Наш кастомный метод ниже
    Student toEntity(StudentRequestDto request);

    @Mapping(target = "email", source = "email.value")   // Распаковываем VO в String
    @Mapping(target = "coins", source = "coins.amount")   // Распаковываем VO в BigDecimal
    StudentResponseDto toDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coins", ignore = true)
    @Mapping(target = "email", source = "email")
    void updateEntityFromDto(StudentRequestDto request, @MappingTarget Student student);


    default Email mapStringToEmail(String value) {
        return value != null ? new Email(value) : null;
    }

    default Money mapBigDecimalToMoney(java.math.BigDecimal value) {
        return value != null ? new Money(value) : null;
    }
}