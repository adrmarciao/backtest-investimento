package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.DividendPayment;
import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Periodicity;
import java.time.LocalDate;
import java.util.List;

public interface PriceFetcherGatewayPort {
    List<HistoricalPrice> fetchHistoricalPrices(String ticker, LocalDate start, LocalDate end, Periodicity periodicity);
    List<DividendPayment> fetchHistoricalDividends(String ticker, LocalDate start, LocalDate end);
}

