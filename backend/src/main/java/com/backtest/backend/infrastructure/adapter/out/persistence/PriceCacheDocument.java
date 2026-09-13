package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.HistoricalPrice;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "price_cache")
public class PriceCacheDocument {

    @Id
    private String id; // composite: TICKER_START_END_PERIODICITY
    private String ticker;
    private List<HistoricalPrice> prices;

    @Indexed(expireAfterSeconds = 86400) // TTL: 24h = 86400s
    private Instant createdAt;

    public PriceCacheDocument() {
    }

    public PriceCacheDocument(String id, String ticker, List<HistoricalPrice> prices, Instant createdAt) {
        this.id = id;
        this.ticker = ticker;
        this.prices = prices;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<HistoricalPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<HistoricalPrice> prices) {
        this.prices = prices;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
