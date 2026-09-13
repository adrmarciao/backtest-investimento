package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.BacktestResult;
import com.backtest.backend.domain.port.in.GetBacktestResultPort;
import com.backtest.backend.domain.port.out.BacktestResultRepositoryPort;

import java.util.List;
import java.util.Optional;

public class GetBacktestResultUseCase implements GetBacktestResultPort {

    private final BacktestResultRepositoryPort repositoryPort;

    public GetBacktestResultUseCase(BacktestResultRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Optional<BacktestResult> getResultById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return repositoryPort.findById(id);
    }

    @Override
    public List<BacktestResult> getAllResults() {
        return repositoryPort.findAll();
    }
}
