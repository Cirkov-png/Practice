package com.practice.lms.common.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void constructor_WhenNegativeAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-1")));
    }

    @Test
    void constructor_WhenNullAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null));
    }

    @Test
    void zero_ShouldReturnMoneyWithZeroAmount() {
        assertEquals(BigDecimal.ZERO, Money.zero().amount());
    }

    @Test
    void add_WhenValidAmounts_ShouldReturnSum() {
        // given
        final var a = new Money(new BigDecimal("100"));
        final var b = new Money(new BigDecimal("50"));

        // when
        final var result = a.add(b);

        // then
        assertEquals(new BigDecimal("150"), result.amount());
    }

    @Test
    void subtract_WhenSufficientBalance_ShouldReturnDifference() {
        // given
        final var a = new Money(new BigDecimal("100"));
        final var b = new Money(new BigDecimal("40"));

        // when
        final var result = a.subtract(b);

        // then
        assertEquals(new BigDecimal("60"), result.amount());
    }

    @Test
    void isLessThan_WhenAmountIsSmaller_ShouldReturnTrue() {
        // given
        final var small = new Money(new BigDecimal("10"));
        final var large = new Money(new BigDecimal("20"));

        // when / then
        assertTrue(small.isLessThan(large));
    }

    @Test
    void isLessThan_WhenAmountIsEqual_ShouldReturnFalse() {
        // given
        final var a = new Money(new BigDecimal("50"));
        final var b = new Money(new BigDecimal("50"));

        // when / then
        assertFalse(a.isLessThan(b));
    }
}
