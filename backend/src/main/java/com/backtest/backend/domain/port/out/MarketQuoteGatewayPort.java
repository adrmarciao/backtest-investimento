package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.MarketQuoteDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MarketQuoteGatewayPort {
    BigDecimal fetchCurrentQuote(String ticker);
    Map<String, BigDecimal> fetchCurrentQuotes(List<String> tickers);
    MarketQuoteDetails fetchQuoteDetails(String ticker);
}

