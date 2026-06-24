package com.practice.lms.common.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class EnrollmentException {

    private EnrollmentException() {}

    public static class AlreadyEnrolled extends LmsException {
        public AlreadyEnrolled(final UUID studentId, final UUID courseId) {
            super("enrollment.already.exists", HttpStatus.CONFLICT, studentId, courseId);
        }
    }
}
