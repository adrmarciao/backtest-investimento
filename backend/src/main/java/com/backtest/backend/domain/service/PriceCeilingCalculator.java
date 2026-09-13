package com.backtest.backend.domain.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PriceCeilingCalculator {

    private static final BigDecimal BAZIN_DIVISOR = new BigDecimal("0.06");
    private static final BigDecimal GRAHAM_MULTIPLIER = new BigDecimal("22.5");

    /**
     * Preço Teto Bazin = DPA / 0.06
     * Retorna null se dpa for nulo ou dpa <= 0
     */
    public static BigDecimal calculateBazin(BigDecimal dpa) {
        if (dpa == null || dpa.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return dpa.divide(BAZIN_DIVISOR, 4, RoundingMode.HALF_UP);
    }

    /**
     * Preço Teto Graham = sqrt(22.5 * LPA * VPA)
     * Retorna null se lpa/vpa forem nulos ou <= 0
     */
    public static BigDecimal calculateGraham(BigDecimal lpa, BigDecimal vpa) {
        if (lpa == null || vpa == null || lpa.compareTo(BigDecimal.ZERO) <= 0 || vpa.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        BigDecimal product = GRAHAM_MULTIPLIER.multiply(lpa).multiply(vpa);
        if (product.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return product.sqrt(new MathContext(10)).setScale(4, RoundingMode.HALF_UP);
    }
}
