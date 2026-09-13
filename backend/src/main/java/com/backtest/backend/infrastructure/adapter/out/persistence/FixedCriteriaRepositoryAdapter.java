package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.FixedCriteria;
import com.backtest.backend.domain.port.out.FixedCriteriaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FixedCriteriaRepositoryAdapter implements FixedCriteriaRepositoryPort {

    private final FixedCriteriaMongoRepository repository;

    public FixedCriteriaRepositoryAdapter(FixedCriteriaMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public FixedCriteria save(FixedCriteria fixedCriteria) {
        FixedCriteriaDocument doc = FixedCriteriaMapper.toDocument(fixedCriteria);
        FixedCriteriaDocument saved = repository.save(doc);
        return FixedCriteriaMapper.toDomain(saved);
    }

    @Override
    public Optional<FixedCriteria> find() {
        return repository.findById("SINGLETON_CRITERIA")
                .map(FixedCriteriaMapper::toDomain);
    }
}
