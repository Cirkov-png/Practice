package com.practice.lms.course.mapper;

import com.practice.lms.common.model.Money;
import com.practice.lms.course.dto.CourseRequestDto;
import com.practice.lms.course.dto.CourseResponseDto;
import com.practice.lms.course.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "coinsPaid", ignore = true) // По умолчанию при создании 0
    @Mapping(target = "price", source = "price")  // Использует mapping метод ниже
    Course toEntity(CourseRequestDto request);

    @Mapping(target = "price", source = "price.amount")
    @Mapping(target = "coinsPaid", source = "coinsPaid.amount") // Мапим из Value Object в BigDecimal
    CourseResponseDto toDto(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "coinsPaid", ignore = true)
    @Mapping(target = "price", source = "price")
    void updateEntityFromDto(CourseRequestDto request, @MappingTarget Course course);

    default Money mapBigDecimalToMoney(BigDecimal value) {
        return value != null ? new Money(value) : null;
    }
}