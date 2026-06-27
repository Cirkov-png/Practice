package com.practice.lms.enrollment.controller;

import com.practice.lms.enrollment.dto.EnrollmentResponseDto;
import com.practice.lms.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @PostMapping("/students/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponseDto enroll(
            @PathVariable final UUID studentId,
            @PathVariable final UUID courseId
    ) {
        return service.enrollStudent(studentId, courseId);
    }
}
