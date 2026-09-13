package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.DividendPayment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "dividend_cache")
public class DividendCacheDocument {

    @Id
    private String id; // composite: TICKER_START_END
    private String ticker;
    private List<DividendPayment> dividends;

    @Indexed(expireAfterSeconds = 86400) // TTL: 24h = 86400s
    private Instant createdAt;

    public DividendCacheDocument() {
    }

    public DividendCacheDocument(String id, String ticker, List<DividendPayment> dividends, Instant createdAt) {
        this.id = id;
        this.ticker = ticker;
        this.dividends = dividends;
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

    public List<DividendPayment> getDividends() {
        return dividends;
    }

    public void setDividends(List<DividendPayment> dividends) {
        this.dividends = dividends;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
