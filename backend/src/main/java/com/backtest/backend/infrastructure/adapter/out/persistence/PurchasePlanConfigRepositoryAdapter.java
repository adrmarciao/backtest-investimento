package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.PurchasePlanConfig;
import com.backtest.backend.domain.port.out.PurchasePlanConfigRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PurchasePlanConfigRepositoryAdapter implements PurchasePlanConfigRepositoryPort {

    private final PurchasePlanConfigMongoRepository repository;

    public PurchasePlanConfigRepositoryAdapter(PurchasePlanConfigMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PurchasePlanConfig save(PurchasePlanConfig config) {
        PurchasePlanConfigDocument doc = PurchasePlanConfigMapper.toDocument(config);
        PurchasePlanConfigDocument saved = repository.save(doc);
        return PurchasePlanConfigMapper.toDomain(saved);
    }

    @Override
    public Optional<PurchasePlanConfig> findDefault() {
        return repository.findById("default")
                .or(() -> repository.findAll().stream().findFirst())
                .map(PurchasePlanConfigMapper::toDomain);
    }
}
