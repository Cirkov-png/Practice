package com.practice.lms.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j // for log
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LmsException.class)
    public ResponseEntity<ApiError> handleLmsException(LmsException ex) {
        log.warn("Business exception occurred. Code: {}, Args: {}", ex.getErrorCode(), ex.getArgs());

        final var error = new ApiError(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getErrorCode(),
                null
        );

        return new ResponseEntity<>(error, ex.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        final var details = new HashMap<String, String>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            final var fieldName = ((FieldError) error).getField();
            final var errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });

        final var error = new ApiError(
                LocalDateTime.now(),
                400,
                "Validation Failed",
                "validation.error",
                details
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(final NoResourceFoundException ex) {
        log.debug("Resource not found: {}", ex.getResourcePath());

        final var error = new ApiError(
                LocalDateTime.now(),
                404,
                "Not Found",
                "resource.not.found",
                null
        );

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex) {
        log.error("Server error occurred: ", ex); //write the trace in the journal

        final var error = new ApiError(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                "internal.server.error",
                null
        );

        return ResponseEntity.internalServerError().body(error);
    }
}