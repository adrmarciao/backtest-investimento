package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BacktestEngineTest {

    private BacktestEngine engine;
    private Asset asset;
    private FixedCriteria criteria;

    @BeforeEach
    void setUp() {
        engine = new BacktestEngine();
        asset = new Asset("WEGE3", new BigDecimal("500.00"), Periodicity.MENSAL);
        criteria = new FixedCriteria(
                new BigDecimal("20.0"),  // PL max 20
                new BigDecimal("5.0"),   // PVP max 5
                new BigDecimal("2.5"),   // Divida/EBITDA max 2.5
                new BigDecimal("15.0")   // ROE min 15%
        );
    }

    @Test
    void testYearEligible_PassesAllCriteria() {
        AnnualIndicators indicators = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("15.0"), // PL <= 20
                new BigDecimal("4.0"),  // PVP <= 5
                new BigDecimal("1.2"),  // Divida/EBITDA <= 2.5
                new BigDecimal("20.0"), // ROE >= 15
                new BigDecimal("1.20"), // DPA -> Bazin = 20.00
                new BigDecimal("2.00"),
                new BigDecimal("10.00") // Graham = sqrt(22.5*2*10) = 21.21
        );

        assertTrue(engine.isYearEligible(indicators, criteria));
    }

    @Test
    void testYearEligible_FailsFixedCriteria() {
        AnnualIndicators indicators = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("25.0"), // PL > 20 (Fails!)
                new BigDecimal("4.0"),
                new BigDecimal("1.2"),
                new BigDecimal("20.0"),
                new BigDecimal("1.20"),
                new BigDecimal("2.00"),
                new BigDecimal("10.00")
        );

        assertFalse(engine.isYearEligible(indicators, criteria));
    }

    @Test
    void testProcessAssetPurchases_EligibleAndPriceBelowCeilings_ExecutesPurchase() {
        AnnualIndicators ind2023 = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("15.0"),
                new BigDecimal("4.0"),
                new BigDecimal("1.2"),
                new BigDecimal("20.0"),
                new BigDecimal("1.20"), // Bazin = 20.00
                new BigDecimal("2.00"),
                new BigDecimal("10.00") // Graham = 21.21
        );

        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2023, 1, 15), new BigDecimal("18.00")), // 18.00 <= 20.00 and 21.21 -> BUY
                new HistoricalPrice(LocalDate.of(2023, 2, 15), new BigDecimal("22.00"))  // 22.00 > 20.00 -> NO BUY
        );

        Set<String> anosIgnorados = new HashSet<>();
        List<Purchase> purchases = engine.processAssetPurchases(asset, criteria, List.of(ind2023), prices, anosIgnorados);

        assertEquals(1, purchases.size());
        Purchase p = purchases.get(0);
        assertEquals(LocalDate.of(2023, 1, 15), p.getData());
        assertEquals("WEGE3", p.getTicker());
        assertEquals(new BigDecimal("18.00"), p.getPreco());
        assertEquals(new BigDecimal("500.00"), p.getValorAportado());
        // 500 / 18 = 27.777778
        assertEquals(new BigDecimal("27.777778"), p.getCotas());
    }

    @Test
    void testProcessAssetPurchases_IneligibleYear_NoPurchasesExecuted() {
        AnnualIndicators ind2023 = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("30.0"), // High PL
                new BigDecimal("4.0"),
                new BigDecimal("1.2"),
                new BigDecimal("20.0"),
                new BigDecimal("1.20"),
                new BigDecimal("2.00"),
                new BigDecimal("10.00")
        );

        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2023, 1, 15), new BigDecimal("10.00"))
        );

        Set<String> anosIgnorados = new HashSet<>();
        List<Purchase> purchases = engine.processAssetPurchases(asset, criteria, List.of(ind2023), prices, anosIgnorados);

        assertTrue(purchases.isEmpty());
        assertFalse(anosIgnorados.isEmpty());
    }

    @Test
    void testProcessAssetPurchasesWithDividends_AccumulatesAndReinvestsWhenCriteriaMet() {
        AnnualIndicators ind2023 = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("15.0"),
                new BigDecimal("4.0"),
                new BigDecimal("1.2"),
                new BigDecimal("20.0"),
                new BigDecimal("1.20"), // Bazin = 20.00
                new BigDecimal("2.00"),
                new BigDecimal("10.00") // Graham = 21.21
        );

        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2023, 1, 15), new BigDecimal("10.00")),
                new HistoricalPrice(LocalDate.of(2023, 2, 15), new BigDecimal("10.00"))
        );

        List<DividendPayment> dividends = List.of(
                new DividendPayment(LocalDate.of(2023, 2, 1), new BigDecimal("1.00"))
        );

        Set<String> anosIgnorados = new HashSet<>();
        AssetBacktestResult result = engine.processAssetPurchasesWithDividends(
                asset, criteria, List.of(ind2023), prices, dividends, anosIgnorados
        );

        List<Purchase> purchases = result.getPurchases();
        assertEquals(3, purchases.size());
        assertEquals(new BigDecimal("50.00"), result.getTotalDividendosRecebidos());
        assertEquals(new BigDecimal("50.00"), result.getTotalDividendosReinvestidos());
        assertEquals(new BigDecimal("0.00"), result.getSaldoCaixaDividendos());

        Purchase reinvested = purchases.get(2);
        assertTrue(reinvested.getIsReinvestimento());
        assertEquals(new BigDecimal("5.000000"), reinvested.getCotas());
    }

    @Test
    void testProcessAssetPurchasesWithDividends_CarriesOverDividendCashWhenCriteriaFails() {
        AnnualIndicators ind2023 = new AnnualIndicators(
                "WEGE3", 2023,
                new BigDecimal("15.0"),
                new BigDecimal("4.0"),
                new BigDecimal("1.2"),
                new BigDecimal("20.0"),
                new BigDecimal("1.20"), // Bazin = 20.00
                new BigDecimal("2.00"),
                new BigDecimal("10.00") // Graham = 21.21
        );

        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2023, 1, 15), new BigDecimal("10.00")),
                new HistoricalPrice(LocalDate.of(2023, 2, 15), new BigDecimal("25.00"))
        );

        List<DividendPayment> dividends = List.of(
                new DividendPayment(LocalDate.of(2023, 2, 1), new BigDecimal("1.00"))
        );

        Set<String> anosIgnorados = new HashSet<>();
        AssetBacktestResult result = engine.processAssetPurchasesWithDividends(
                asset, criteria, List.of(ind2023), prices, dividends, anosIgnorados
        );

        assertEquals(1, result.getPurchases().size());
        assertEquals(new BigDecimal("50.00"), result.getTotalDividendosRecebidos());
        assertEquals(new BigDecimal("0.00"), result.getTotalDividendosReinvestidos());
        assertEquals(new BigDecimal("50.00"), result.getSaldoCaixaDividendos());
    }

    @Test
    void testYearEligible_NullIndicatorValues_IgnoredAndPasses() {
        // BBSE3 / Seguradoras: Divida/EBITDA is null
        AnnualIndicators indicators = new AnnualIndicators(
                "BBSE3", 2023,
                new BigDecimal("11.5"), // PL <= 20
                new BigDecimal("4.2"),  // PVP <= 5
                null,                   // Divida/EBITDA null -> Ignored!
                new BigDecimal("75.0"), // ROE >= 15
                new BigDecimal("2.10"), // DPA -> Bazin = 35.00
                null,                   // LPA null -> Graham null
                null                    // VPA null
        );

        assertTrue(engine.isYearEligible(indicators, criteria));
    }

    @Test
    void testProcessAssetPurchases_PartialIndicatorsRegisteredYear_ExecutesPurchaseIfPriceBelowBazin() {
        AnnualIndicators indBBSE3 = new AnnualIndicators(
                "BBSE3", 2023,
                new BigDecimal("11.5"),
                new BigDecimal("4.2"),
                null,                   // Divida/EBITDA null
                new BigDecimal("75.0"),
                new BigDecimal("2.10"), // Bazin = 35.00
                null,                   // LPA null
                null                    // VPA null (Graham ignored)
        );

        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2023, 1, 15), new BigDecimal("30.00")) // 30 <= 35 -> BUY
        );

        Set<String> anosIgnorados = new HashSet<>();
        List<Purchase> purchases = engine.processAssetPurchases(asset, criteria, List.of(indBBSE3), prices, anosIgnorados);

        assertEquals(1, purchases.size());
        assertEquals(new BigDecimal("30.00"), purchases.get(0).getPreco());
    }

    @Test
    void testProcessAssetPurchases_UnregisteredYear_NoPurchasesExecuted() {
        // Year 2024 has NO indicators registered
        List<HistoricalPrice> prices = List.of(
                new HistoricalPrice(LocalDate.of(2024, 1, 15), new BigDecimal("10.00"))
        );

        Set<String> anosIgnorados = new HashSet<>();
        List<Purchase> purchases = engine.processAssetPurchases(asset, criteria, Collections.emptyList(), prices, anosIgnorados);

        assertTrue(purchases.isEmpty());
        assertTrue(anosIgnorados.contains("WEGE3 - 2024 (Sem dados fundamentalistas)"));
    }
}


