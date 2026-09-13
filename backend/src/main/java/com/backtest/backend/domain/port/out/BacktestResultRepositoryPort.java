package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.BacktestResult;
import java.util.List;
import java.util.Optional;

public interface BacktestResultRepositoryPort {
    BacktestResult save(BacktestResult result);
    Optional<BacktestResult> findById(String id);
    List<BacktestResult> findAll();
}
