package com.practice.lms.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record Money(
        @Column(nullable = false)
        BigDecimal amount
) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("money.cannot.be.negative");
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
}