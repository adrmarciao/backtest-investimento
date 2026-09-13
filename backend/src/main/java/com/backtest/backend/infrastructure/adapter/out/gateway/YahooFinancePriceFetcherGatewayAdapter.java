package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.DividendPayment;
import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Periodicity;
import com.backtest.backend.domain.port.out.PriceFetcherGatewayPort;
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

    public YahooFinancePriceFetcherGatewayAdapter(YahooFinanceClient yahooFinanceClient, PriceCacheMongoRepository cacheRepository) {
        this.yahooFinanceClient = yahooFinanceClient;
        this.cacheRepository = cacheRepository;
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
        return yahooFinanceClient.fetchDividends(ticker, start, end);
    }

    private String buildCacheKey(String ticker, LocalDate start, LocalDate end, Periodicity periodicity) {
        String safeTicker = ticker != null ? ticker.trim().toUpperCase() : "UNKNOWN";
        return safeTicker + "_" + start + "_" + end + "_" + periodicity;
    }
}

