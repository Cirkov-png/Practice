package com.practice.lms.common.exception;

import org.springframework.http.HttpStatus;
import java.util.UUID;

public final class StudentException {

    private StudentException() {}

    public static class NotFound extends LmsException {
        public NotFound(UUID id) {
            super("student.not.found", HttpStatus.NOT_FOUND, id);
        }
    }

    public static class EmailAlreadyExists extends LmsException {
        public EmailAlreadyExists(String email) {
            super("student.email.already.exists", HttpStatus.BAD_REQUEST, email);
        }
    }

    public static class InsufficientFunds extends LmsException {
        public InsufficientFunds(UUID id) {
            super("student.insufficient.funds", HttpStatus.BAD_REQUEST, id);
        }
    }
}