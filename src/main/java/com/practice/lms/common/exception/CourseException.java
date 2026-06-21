package com.practice.lms.common.exception;

import org.springframework.http.HttpStatus;
import java.util.UUID;

public final class CourseException {

    private CourseException() {}

    public static class NotFound extends LmsException {
        public NotFound(UUID id) {
            super("course.not.found", HttpStatus.NOT_FOUND, id);
        }
    }

    public static class SettingsNotFound extends LmsException {
        public SettingsNotFound(UUID courseId) {
            super("course.settings.not.found", HttpStatus.NOT_FOUND, courseId);
        }
    }
}