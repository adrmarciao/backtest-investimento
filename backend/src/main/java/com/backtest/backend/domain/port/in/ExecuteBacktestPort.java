package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.BacktestResult;
import java.time.LocalDate;

public interface ExecuteBacktestPort {
    BacktestResult executeBacktest(LocalDate start, LocalDate end);
}
