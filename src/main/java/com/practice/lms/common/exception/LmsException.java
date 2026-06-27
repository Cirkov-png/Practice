package com.practice.lms.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LmsException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;
    private final Object[] args;

    protected LmsException(String errorCode, HttpStatus status, Object... args) {
        super(errorCode); // Using the error code as a technical message
        this.errorCode = errorCode;
        this.status = status;
        this.args = args;
    }
}