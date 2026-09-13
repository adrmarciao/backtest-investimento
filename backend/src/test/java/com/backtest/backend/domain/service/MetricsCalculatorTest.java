package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MetricsCalculatorTest {

    private MetricsCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new MetricsCalculator();
    }

    @Test
    void testCalculateTotalInvested() {
        List<Purchase> purchases = List.of(
                new Purchase(LocalDate.now(), "WEGE3", new BigDecimal("10.00"), new BigDecimal("500.00"), new BigDecimal("50"), null, null),
                new Purchase(LocalDate.now(), "WEGE3", new BigDecimal("12.00"), new BigDecimal("500.00"), new BigDecimal("41.666667"), null, null)
        );

        BigDecimal total = calculator.calculateTotalInvested(purchases);
        assertEquals(new BigDecimal("1000.00"), total);
    }

    @Test
    void testCalculateTotalReturn() {
        BigDecimal totalInvested = new BigDecimal("1000.00");
        BigDecimal finalValue = new BigDecimal("1250.00");

        BigDecimal totalReturn = calculator.calculateTotalReturn(totalInvested, finalValue);
        assertEquals(new BigDecimal("25.00"), totalReturn);
    }

    @Test
    void testCalculateCAGR() {
        BigDecimal totalInvested = new BigDecimal("1000.00");
        BigDecimal finalValue = new BigDecimal("1210.00"); // ~10% a.a. over 2 years
        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);

        BigDecimal cagr = calculator.calculateCAGR(totalInvested, finalValue, start, end);
        assertEquals(new BigDecimal("10.01"), cagr);
    }

    @Test
    void testCalculateMaxDrawdown() {
        List<TimeSeriesPoint> timeSeries = List.of(
                new TimeSeriesPoint(LocalDate.of(2023, 1, 1), new BigDecimal("1000"), new BigDecimal("1000"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                new TimeSeriesPoint(LocalDate.of(2023, 2, 1), new BigDecimal("1000"), new BigDecimal("1200"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), // Peak 1200
                new TimeSeriesPoint(LocalDate.of(2023, 3, 1), new BigDecimal("1000"), new BigDecimal("900"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),  // Drop to 900 -> Drawdown = (1200-900)/1200 = 25%
                new TimeSeriesPoint(LocalDate.of(2023, 4, 1), new BigDecimal("1000"), new BigDecimal("1100"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        );

        BigDecimal maxDd = calculator.calculateMaxDrawdown(timeSeries);
        assertEquals(new BigDecimal("25.00"), maxDd);
    }

    @Test
    void testCalculateAssetSummaries() {
        List<Purchase> purchases = List.of(
                new Purchase(LocalDate.now(), "WEGE3", new BigDecimal("10.00"), new BigDecimal("500.00"), new BigDecimal("50.0"), null, null),
                new Purchase(LocalDate.now(), "WEGE3", new BigDecimal("20.00"), new BigDecimal("500.00"), new BigDecimal("25.0"), null, null)
        );

        Map<String, BigDecimal> currentPrices = Map.of("WEGE3", new BigDecimal("20.00"));
        List<AssetSummary> summaries = calculator.calculateAssetSummaries(purchases, currentPrices);

        assertEquals(1, summaries.size());
        AssetSummary summary = summaries.get(0);
        assertEquals("WEGE3", summary.getTicker());
        assertEquals(new BigDecimal("1000.00"), summary.getTotalAportado());
        assertEquals(new BigDecimal("75.0"), summary.getTotalCotas());
        // Price average = 1000 / 75 = 13.3333
        assertEquals(new BigDecimal("13.3333"), summary.getPrecoMedio());
        // Current value = 75 * 20 = 1500.00
        assertEquals(new BigDecimal("1500.00"), summary.getValorAtual());
        // Return pct = (1500 - 1000) / 1000 * 100 = 50.00%
        assertEquals(new BigDecimal("50.00"), summary.getRetornoPercentual());
    }
}
