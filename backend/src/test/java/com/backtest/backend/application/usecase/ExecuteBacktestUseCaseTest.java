package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Purchase;
import com.backtest.backend.domain.entity.TimeSeriesPoint;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecuteBacktestUseCaseTest {

    @Test
    void buildTimeSeries_shouldExcludeDividendReinvestmentsFromAccumulatedInvested() throws Exception {
        // Arrange
        ExecuteBacktestUseCase useCase = new ExecuteBacktestUseCase(null, null, null, null, null);
        Method buildTimeSeries = ExecuteBacktestUseCase.class.getDeclaredMethod(
                "buildTimeSeries", List.class, Map.class, List.class, LocalDate.class, LocalDate.class);
        buildTimeSeries.setAccessible(true);

        LocalDate date1 = LocalDate.of(2023, 1, 1);
        LocalDate date2 = LocalDate.of(2023, 2, 1);

        // Regular purchase: 100 cotas at R$ 10 (Total: 1000)
        Purchase regularPurchase = new Purchase(date1, "AAPL", new BigDecimal("10"), new BigDecimal("1000"), new BigDecimal("100"), null, null, false);
        // Dividend reinvestment purchase: 10 cotas at R$ 12 (Total: 120)
        Purchase divPurchase = new Purchase(date2, "AAPL", new BigDecimal("12"), new BigDecimal("120"), new BigDecimal("10"), null, null, true);

        List<Purchase> purchases = Arrays.asList(regularPurchase, divPurchase);

        // Prices for the asset
        HistoricalPrice price1 = new HistoricalPrice(date1, new BigDecimal("10"));
        HistoricalPrice price2 = new HistoricalPrice(date2, new BigDecimal("12"));
        Map<String, List<HistoricalPrice>> pricesByTicker = new HashMap<>();
        pricesByTicker.put("AAPL", Arrays.asList(price1, price2));

        // Prices for IBOV
        HistoricalPrice ibov1 = new HistoricalPrice(date1, new BigDecimal("100000"));
        HistoricalPrice ibov2 = new HistoricalPrice(date2, new BigDecimal("110000"));
        List<HistoricalPrice> ibovPrices = Arrays.asList(ibov1, ibov2);

        // Act
        @SuppressWarnings("unchecked")
        List<TimeSeriesPoint> result = (List<TimeSeriesPoint>) buildTimeSeries.invoke(
                useCase, purchases, pricesByTicker, ibovPrices, date1, date2);

        // Assert
        assertEquals(2, result.size());
        
        // Point 1 (date1)
        TimeSeriesPoint p1 = result.get(0);
        // accumulatedInvested should be 1000 (regular purchase)
        assertEquals(new BigDecimal("1000.00"), p1.getValorInvestidoAcumulado());
        // currentPortfolioValue should be 1000 (100 * 10)
        assertEquals(new BigDecimal("1000.00"), p1.getValorPatrimonio());
        // Normalization base 100: (1000 / 1000) * 100 = 100
        assertEquals(new BigDecimal("100.00"), p1.getPatrimonioNormalizado());

        // Point 2 (date2)
        TimeSeriesPoint p2 = result.get(1);
        // accumulatedInvested should STILL be 1000, not 1120, because the second purchase was a reinvestment
        assertEquals(new BigDecimal("1000.00"), p2.getValorInvestidoAcumulado());
        // currentPortfolioValue should be 1320 ((100 + 10) * 12)
        assertEquals(new BigDecimal("1320.00"), p2.getValorPatrimonio());
        // Normalization base 100: (1320 / 1000) * 100 = 132.00
        assertEquals(new BigDecimal("132.00"), p2.getPatrimonioNormalizado());
    }
}
