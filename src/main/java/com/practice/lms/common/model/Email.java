package com.practice.lms.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Email(
        @Column(name = "email", nullable = false, unique = true)
        String value
) {
    public Email {
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("invalid.email.format");
        }
    }
}