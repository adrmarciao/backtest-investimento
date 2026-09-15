package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.port.out.MarketQuoteGatewayPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MarketQuoteGatewayAdapter implements MarketQuoteGatewayPort {

    private final GoogleFinanceClient googleFinanceClient;

    public MarketQuoteGatewayAdapter(GoogleFinanceClient googleFinanceClient) {
        this.googleFinanceClient = googleFinanceClient;
    }

    @Override
    public BigDecimal fetchCurrentQuote(String ticker) {
        return googleFinanceClient.fetchQuote(ticker);
    }

    @Override
    public Map<String, BigDecimal> fetchCurrentQuotes(List<String> tickers) {
        Map<String, BigDecimal> quotes = new HashMap<>();
        if (tickers == null) return quotes;
        for (String ticker : tickers) {
            BigDecimal price = googleFinanceClient.fetchQuote(ticker);
            if (price != null) {
                quotes.put(ticker.trim().toUpperCase(), price);
            }
        }
        return quotes;
    }
}
