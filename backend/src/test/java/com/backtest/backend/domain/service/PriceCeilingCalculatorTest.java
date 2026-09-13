package com.backtest.backend.domain.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceCeilingCalculatorTest {

    @Test
    void testCalculateBazin_ValidDpa() {
        BigDecimal dpa = new BigDecimal("1.20");
        BigDecimal result = PriceCeilingCalculator.calculateBazin(dpa);
        assertNotNull(result);
        assertEquals(new BigDecimal("20.0000"), result);
    }

    @Test
    void testCalculateBazin_ZeroOrNegative() {
        assertNull(PriceCeilingCalculator.calculateBazin(BigDecimal.ZERO));
        assertNull(PriceCeilingCalculator.calculateBazin(new BigDecimal("-0.5")));
        assertNull(PriceCeilingCalculator.calculateBazin(null));
    }

    @Test
    void testCalculateGraham_ValidLpaVpa() {
        // sqrt(22.5 * 2.0 * 10.0) = sqrt(450) = 21.2132
        BigDecimal lpa = new BigDecimal("2.0");
        BigDecimal vpa = new BigDecimal("10.0");
        BigDecimal result = PriceCeilingCalculator.calculateGraham(lpa, vpa);
        assertNotNull(result);
        assertEquals(new BigDecimal("21.2132"), result);
    }

    @Test
    void testCalculateGraham_InvalidInputs() {
        assertNull(PriceCeilingCalculator.calculateGraham(BigDecimal.ZERO, new BigDecimal("10.0")));
        assertNull(PriceCeilingCalculator.calculateGraham(new BigDecimal("2.0"), new BigDecimal("-1.0")));
        assertNull(PriceCeilingCalculator.calculateGraham(null, new BigDecimal("10.0")));
    }
}
