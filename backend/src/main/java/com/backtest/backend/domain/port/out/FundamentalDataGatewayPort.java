package com.backtest.backend.domain.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FundamentalDataGatewayPort {
    record FundamentalData(BigDecimal lpa, BigDecimal vpa) {}
    Map<String, FundamentalData> fetchFundamentals(List<String> tickers, String token);
}
