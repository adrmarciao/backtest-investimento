package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchasePlanCalculatorServiceTest {

    private PurchasePlanCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new PurchasePlanCalculatorService();
    }

    @Test
    @DisplayName("Cálculo automático de parcela mensal: R$ 60.000 / 12 meses = R$ 5.000,00")
    void shouldCalculateMonthlyInstallment() {
        PurchasePlanConfig config = new PurchasePlanConfig(
                "default",
                new BigDecimal("60000.00"),
                12,
                Periodicity.MENSAL,
                null,
                "IBOV",
                null,
                null,
                null,
                null
        );

        BigDecimal aporte = config.calculateAporteRodada();
        assertEquals(new BigDecimal("5000.00"), aporte);
    }

    @Test
    @DisplayName("Cálculo automático de parcela semanal: R$ 77.702 / 12 meses / 4 = R$ 1.618,79")
    void shouldCalculateWeeklyInstallment() {
        PurchasePlanConfig config = new PurchasePlanConfig(
                "default",
                new BigDecimal("77702.00"),
                12,
                Periodicity.SEMANAL,
                null,
                "IBOV",
                null,
                null,
                null,
                null
        );

        BigDecimal aporte = config.calculateAporteRodada();
        assertEquals(new BigDecimal("1618.79"), aporte);
    }

    @Test
    @DisplayName("Sobrescrita manual do aporte da rodada")
    void shouldRespectManualInstallmentOverride() {
        PurchasePlanConfig config = new PurchasePlanConfig(
                "default",
                new BigDecimal("60000.00"),
                12,
                Periodicity.MENSAL,
                new BigDecimal("2500.00"),
                "IBOV",
                null,
                null,
                null,
                null
        );

        BigDecimal aporte = config.calculateAporteRodada();
        assertEquals(new BigDecimal("2500.00"), aporte);
    }

    @Test
    @DisplayName("Rateio proporcional de compras com cálculo de quantidades por lote e sobras de caixa")
    void shouldCalculateProportionalAllocationAndCashRemainder() {
        PurchasePlanConfig config = new PurchasePlanConfig(
                "default",
                new BigDecimal("60000.00"),
                12,
                Periodicity.MENSAL,
                new BigDecimal("5000.00"),
                "IBOV",
                null,
                null,
                null,
                null
        );

        // Ativo 1: peso 2, preco 20.00
        PurchasePlanAsset a1 = new PurchasePlanAsset();
        a1.setTicker("ITSA4");
        a1.setPrecoAtual(new BigDecimal("20.00"));
        a1.setPeso(new BigDecimal("2.0"));
        a1.setHabilitado(BigDecimal.ONE);
        a1.setAjusteManualQtd(0);

        // Ativo 2: peso 1, preco 30.00
        PurchasePlanAsset a2 = new PurchasePlanAsset();
        a2.setTicker("BBAS3");
        a2.setPrecoAtual(new BigDecimal("30.00"));
        a2.setPeso(new BigDecimal("1.0"));
        a2.setHabilitado(BigDecimal.ONE);
        a2.setAjusteManualQtd(1); // +1 ajuste manual

        RoundAllocationResult result = calculatorService.calculateAllocation(config, List.of(a1, a2));

        assertNotNull(result);
        assertEquals(new BigDecimal("5000.00"), result.aporteRodada());
        assertEquals(2, result.itens().size());

        AssetAllocationItem itemA1 = result.itens().get(0);
        // A1: 2/3 de 5000 = 3333.33 -> floor(3333.33 / 20) = 166 ações -> 166 * 20 = 3320.00
        assertEquals("ITSA4", itemA1.ticker());
        assertEquals(166, itemA1.qtdSugerida());
        assertEquals(166, itemA1.qtdFinal());
        assertEquals(new BigDecimal("3320.00"), itemA1.totalGasto());

        AssetAllocationItem itemA2 = result.itens().get(1);
        // A2: 1/3 de 5000 = 1666.67 -> floor(1666.67 / 30) = 55 ações -> +1 manual = 56 ações -> 56 * 30 = 1680.00
        assertEquals("BBAS3", itemA2.ticker());
        assertEquals(55, itemA2.qtdSugerida());
        assertEquals(56, itemA2.qtdFinal());
        assertEquals(new BigDecimal("1680.00"), itemA2.totalGasto());

        // Total Gasto = 3320 + 1680 = 5000.00 -> Sobra = 0.00
        assertEquals(new BigDecimal("5000.00"), result.totalGasto());
        assertEquals(new BigDecimal("0.00"), result.sobraCaixa());
        assertNotNull(result.boletaTexto());
        assertTrue(result.boletaTexto().contains("BOLETA DE ORDENS"));
    }

    @Test
    @DisplayName("Termômetro macro: cálculo de Drawdown e retração Fibonacci")
    void shouldCalculateDrawdownAndFibonacci() {
        BigDecimal precoAtual = new BigDecimal("125000.00");
        BigDecimal altaAno = new BigDecimal("135000.00");
        BigDecimal fiboUp = new BigDecimal("135000.00");
        BigDecimal fiboDown = new BigDecimal("115000.00");

        BenchmarkStatus status = calculatorService.calculateBenchmarkStatus(
                "IBOV",
                precoAtual,
                altaAno,
                fiboUp,
                fiboDown
        );

        assertNotNull(status);
        assertEquals("IBOV", status.benchmark());
        // Drawdown: (125000 - 135000) / 135000 * 100 = -7.41%
        assertEquals(new BigDecimal("-7.41"), status.drawdownPercent());
        // Fibo: (135000 - 125000) / 20000 * 100 = 50.00%
        assertEquals(new BigDecimal("50.00"), status.fiboRetractionPercent());
    }
}
