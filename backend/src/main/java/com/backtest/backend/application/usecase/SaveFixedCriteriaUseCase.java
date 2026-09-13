package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.FixedCriteria;
import com.backtest.backend.domain.port.in.SaveFixedCriteriaPort;
import com.backtest.backend.domain.port.out.FixedCriteriaRepositoryPort;

import java.util.Optional;

public class SaveFixedCriteriaUseCase implements SaveFixedCriteriaPort {

    private final FixedCriteriaRepositoryPort repositoryPort;

    public SaveFixedCriteriaUseCase(FixedCriteriaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public FixedCriteria saveCriteria(FixedCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Critérios não podem ser nulos");
        }
        return repositoryPort.save(criteria);
    }

    @Override
    public Optional<FixedCriteria> getCriteria() {
        return repositoryPort.find();
    }
}
