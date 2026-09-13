package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.BacktestResult;
import java.util.List;
import java.util.Optional;

public interface GetBacktestResultPort {
    Optional<BacktestResult> getResultById(String id);
    List<BacktestResult> getAllResults();
}
