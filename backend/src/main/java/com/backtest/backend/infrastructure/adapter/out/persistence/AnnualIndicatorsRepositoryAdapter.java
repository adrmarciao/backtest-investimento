package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.AnnualIndicators;
import com.backtest.backend.domain.port.out.AnnualIndicatorsRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AnnualIndicatorsRepositoryAdapter implements AnnualIndicatorsRepositoryPort {

    private final AnnualIndicatorsMongoRepository repository;

    public AnnualIndicatorsRepositoryAdapter(AnnualIndicatorsMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public AnnualIndicators save(AnnualIndicators indicators) {
        AnnualIndicatorsDocument doc = AnnualIndicatorsMapper.toDocument(indicators);
        AnnualIndicatorsDocument saved = repository.save(doc);
        return AnnualIndicatorsMapper.toDomain(saved);
    }

    @Override
    public Optional<AnnualIndicators> findByTickerAndAno(String ticker, int ano) {
        if (ticker == null) return Optional.empty();
        return repository.findByTickerAndAno(ticker.trim().toUpperCase(), ano)
                .map(AnnualIndicatorsMapper::toDomain);
    }

    @Override
    public List<AnnualIndicators> findByTicker(String ticker) {
        if (ticker == null) return List.of();
        return repository.findByTicker(ticker.trim().toUpperCase()).stream()
                .map(AnnualIndicatorsMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByTickerAndAno(String ticker, int ano) {
        if (ticker != null) {
            repository.deleteByTickerAndAno(ticker.trim().toUpperCase(), ano);
        }
    }

    @Override
    public void deleteByTicker(String ticker) {
        if (ticker != null) {
            repository.deleteByTicker(ticker.trim().toUpperCase());
        }
    }
}
