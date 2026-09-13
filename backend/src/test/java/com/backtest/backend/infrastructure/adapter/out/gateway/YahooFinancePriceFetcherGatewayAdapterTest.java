package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.DividendPayment;
import com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheDocument;
import com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheMongoRepository;
import com.backtest.backend.infrastructure.adapter.out.persistence.PriceCacheMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YahooFinancePriceFetcherGatewayAdapterTest {

    @Mock
    private YahooFinanceClient yahooFinanceClient;

    @Mock
    private PriceCacheMongoRepository priceCacheRepository;

    @Mock
    private DividendCacheMongoRepository dividendCacheRepository;

    @InjectMocks
    private YahooFinancePriceFetcherGatewayAdapter adapter;

    @Test
    void fetchHistoricalDividends_whenCacheHit_shouldReturnCachedDividendsAndNotCallClient() {
        // Arrange
        String ticker = "PETR4.SA";
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        String expectedCacheKey = "PETR4.SA_2023-01-01_2023-12-31";

        List<DividendPayment> cachedDividends = List.of(
                new DividendPayment(LocalDate.of(2023, 5, 15), new BigDecimal("1.2500"))
        );
        DividendCacheDocument cacheDoc = new DividendCacheDocument(expectedCacheKey, ticker, cachedDividends, Instant.now());

        when(dividendCacheRepository.findById(expectedCacheKey)).thenReturn(Optional.of(cacheDoc));

        // Act
        List<DividendPayment> result = adapter.fetchHistoricalDividends(ticker, start, end);

        // Assert
        assertEquals(cachedDividends, result);
        verify(dividendCacheRepository).findById(expectedCacheKey);
        verifyNoInteractions(yahooFinanceClient);
        verify(dividendCacheRepository, never()).save(any());
    }

    @Test
    void fetchHistoricalDividends_whenCacheMiss_shouldFetchFromClientAndSaveToCache() {
        // Arrange
        String ticker = "PETR4.SA";
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        String expectedCacheKey = "PETR4.SA_2023-01-01_2023-12-31";

        List<DividendPayment> fetchedDividends = List.of(
                new DividendPayment(LocalDate.of(2023, 5, 15), new BigDecimal("1.2500"))
        );

        when(dividendCacheRepository.findById(expectedCacheKey)).thenReturn(Optional.empty());
        when(yahooFinanceClient.fetchDividends(ticker, start, end)).thenReturn(fetchedDividends);

        // Act
        List<DividendPayment> result = adapter.fetchHistoricalDividends(ticker, start, end);

        // Assert
        assertEquals(fetchedDividends, result);
        verify(dividendCacheRepository).findById(expectedCacheKey);
        verify(yahooFinanceClient).fetchDividends(ticker, start, end);

        ArgumentCaptor<DividendCacheDocument> captor = ArgumentCaptor.forClass(DividendCacheDocument.class);
        verify(dividendCacheRepository).save(captor.capture());

        DividendCacheDocument savedDoc = captor.getValue();
        assertEquals(expectedCacheKey, savedDoc.getId());
        assertEquals(ticker, savedDoc.getTicker());
        assertEquals(fetchedDividends, savedDoc.getDividends());
        assertNotNull(savedDoc.getCreatedAt());
    }

    @Test
    void fetchHistoricalDividends_whenCacheMissAndClientReturnsEmpty_shouldSaveEmptyListToCache() {
        // Arrange
        String ticker = "VALE3.SA";
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 6, 30);
        String expectedCacheKey = "VALE3.SA_2023-01-01_2023-06-30";

        when(dividendCacheRepository.findById(expectedCacheKey)).thenReturn(Optional.empty());
        when(yahooFinanceClient.fetchDividends(ticker, start, end)).thenReturn(Collections.emptyList());

        // Act
        List<DividendPayment> result = adapter.fetchHistoricalDividends(ticker, start, end);

        // Assert
        assertTrue(result.isEmpty());
        verify(dividendCacheRepository).findById(expectedCacheKey);
        verify(yahooFinanceClient).fetchDividends(ticker, start, end);

        ArgumentCaptor<DividendCacheDocument> captor = ArgumentCaptor.forClass(DividendCacheDocument.class);
        verify(dividendCacheRepository).save(captor.capture());

        DividendCacheDocument savedDoc = captor.getValue();
        assertEquals(expectedCacheKey, savedDoc.getId());
        assertTrue(savedDoc.getDividends().isEmpty());
    }
}
