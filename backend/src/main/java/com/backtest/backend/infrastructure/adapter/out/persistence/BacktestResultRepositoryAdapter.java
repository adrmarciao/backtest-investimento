package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.BacktestResult;
import com.backtest.backend.domain.port.out.BacktestResultRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BacktestResultRepositoryAdapter implements BacktestResultRepositoryPort {

    private final BacktestResultMongoRepository repository;

    public BacktestResultRepositoryAdapter(BacktestResultMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public BacktestResult save(BacktestResult result) {
        BacktestResultDocument doc = BacktestResultMapper.toDocument(result);
        BacktestResultDocument saved = repository.save(doc);
        return BacktestResultMapper.toDomain(saved);
    }

    @Override
    public Optional<BacktestResult> findById(String id) {
        if (id == null) return Optional.empty();
        return repository.findById(id).map(BacktestResultMapper::toDomain);
    }

    @Override
    public List<BacktestResult> findAll() {
        return repository.findAll().stream()
                .map(BacktestResultMapper::toDomain)
                .collect(Collectors.toList());
    }
}
