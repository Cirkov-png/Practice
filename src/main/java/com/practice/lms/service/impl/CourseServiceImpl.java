package com.practice.lms.service.impl;

import com.practice.lms.dto.course.CourseRequestDto;
import com.practice.lms.dto.course.CourseResponseDto;
import com.practice.lms.dto.settings.CourseSettingsRequestDto;
import com.practice.lms.entity.Course;
import com.practice.lms.entity.Student;
import com.practice.lms.exception.ResourceNotFoundException;
import com.practice.lms.mapper.CourseMapper;
import com.practice.lms.repository.CourseRepository;
import com.practice.lms.repository.StudentRepository;
import com.practice.lms.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class CourseServiceImpl implements CourseService {

    final private CourseRepository repository;
    final private CourseMapper mapper;
    private final StudentRepository studentRepository;


    @Override
    public CourseResponseDto create(CourseRequestDto request){
        Course course = mapper.toEntity(request);
        return mapper.toDto(repository.save(course));
    }
    @Override
    public CourseResponseDto getById(Long id){

        Optional<Course> optionalCourse = repository.findById(id);

        if (optionalCourse.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
        Course course = optionalCourse.get();

        return mapper.toDto(course);
    }

    @Override
    public List<CourseResponseDto> getAll() {
        List<CourseResponseDto> finalDtoList = repository.findAll().stream().map(course -> mapper.toDto(course)).toList();
        return finalDtoList;

    }

    @Override
    public CourseResponseDto update(Long id, CourseRequestDto request){
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isEmpty()){
            throw new ResourceNotFoundException("Course not found with id " + id);

        }
        Course course = optionalCourse.get();
        course.setTitle(request.getTitle());
        course.setStartDate(request.getStartDate());
        course.setPrice(request.getPrice());
        course.setDescription(request.getDescription());
        return mapper.toDto(repository.save(course));

    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional // Важно! Операция изменяет данные в промежуточной таблице
    public void enrollStudent(Long courseId, Long studentId) {
        Course course = repository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с id " + courseId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент не найден с id " + studentId));

        course.addStudent(student);
        repository.save(course);
    }

}
