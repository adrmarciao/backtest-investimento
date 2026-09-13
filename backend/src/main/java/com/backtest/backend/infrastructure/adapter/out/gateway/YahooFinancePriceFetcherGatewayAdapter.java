package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.DividendPayment;
import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Periodicity;
import com.backtest.backend.domain.port.out.PriceFetcherGatewayPort;
import com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheDocument;
import com.backtest.backend.infrastructure.adapter.out.persistence.DividendCacheMongoRepository;
import com.backtest.backend.infrastructure.adapter.out.persistence.PriceCacheDocument;
import com.backtest.backend.infrastructure.adapter.out.persistence.PriceCacheMongoRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class YahooFinancePriceFetcherGatewayAdapter implements PriceFetcherGatewayPort {

    private final YahooFinanceClient yahooFinanceClient;
    private final PriceCacheMongoRepository cacheRepository;
    private final DividendCacheMongoRepository dividendCacheRepository;

    public YahooFinancePriceFetcherGatewayAdapter(YahooFinanceClient yahooFinanceClient,
                                                 PriceCacheMongoRepository cacheRepository,
                                                 DividendCacheMongoRepository dividendCacheRepository) {
        this.yahooFinanceClient = yahooFinanceClient;
        this.cacheRepository = cacheRepository;
        this.dividendCacheRepository = dividendCacheRepository;
    }

    @Override
    public List<HistoricalPrice> fetchHistoricalPrices(String ticker, LocalDate start, LocalDate end, Periodicity periodicity) {
        String cacheKey = buildCacheKey(ticker, start, end, periodicity);

        // 1. Verificar no Cache
        Optional<PriceCacheDocument> cached = cacheRepository.findById(cacheKey);
        if (cached.isPresent() && cached.get().getPrices() != null && !cached.get().getPrices().isEmpty()) {
            return cached.get().getPrices();
        }

        // 2. Cache miss -> Buscar no Yahoo Finance
        List<HistoricalPrice> prices = yahooFinanceClient.fetchPrices(ticker, start, end, periodicity);

        // 3. Salvar no cache se houver dados
        if (!prices.isEmpty()) {
            PriceCacheDocument cacheDoc = new PriceCacheDocument(cacheKey, ticker, prices, Instant.now());
            cacheRepository.save(cacheDoc);
        }

        return prices;
    }

    @Override
    public List<DividendPayment> fetchHistoricalDividends(String ticker, LocalDate start, LocalDate end) {
        String cacheKey = buildDividendCacheKey(ticker, start, end);

        // 1. Verificar no Cache
        Optional<DividendCacheDocument> cached = dividendCacheRepository.findById(cacheKey);
        if (cached.isPresent() && cached.get().getDividends() != null) {
            return cached.get().getDividends();
        }

        // 2. Cache miss -> Buscar no Yahoo Finance
        List<DividendPayment> dividends = yahooFinanceClient.fetchDividends(ticker, start, end);

        // 3. Salvar no cache se não nulo (incluindo lista vazia para evitar cache stampede)
        if (dividends != null) {
            DividendCacheDocument cacheDoc = new DividendCacheDocument(cacheKey, ticker, dividends, Instant.now());
            dividendCacheRepository.save(cacheDoc);
        }

        return dividends;
    }

    private String buildCacheKey(String ticker, LocalDate start, LocalDate end, Periodicity periodicity) {
        String safeTicker = ticker != null ? ticker.trim().toUpperCase() : "UNKNOWN";
        return safeTicker + "_" + start + "_" + end + "_" + periodicity;
    }

    private String buildDividendCacheKey(String ticker, LocalDate start, LocalDate end) {
        String safeTicker = ticker != null ? ticker.trim().toUpperCase() : "UNKNOWN";
        return safeTicker + "_" + start + "_" + end;
    }
}
