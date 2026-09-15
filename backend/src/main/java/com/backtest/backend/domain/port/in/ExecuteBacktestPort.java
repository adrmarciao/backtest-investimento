package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.BacktestResult;
import java.time.LocalDate;
import java.util.List;

public interface ExecuteBacktestPort {
    BacktestResult executeBacktest(LocalDate start, LocalDate end, List<String> tickers);

    default BacktestResult executeBacktest(LocalDate start, LocalDate end) {
        return executeBacktest(start, end, null);
    }
}

