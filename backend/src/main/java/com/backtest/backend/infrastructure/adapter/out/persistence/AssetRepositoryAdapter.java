package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.Asset;
import com.backtest.backend.domain.port.out.AssetRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetRepositoryAdapter implements AssetRepositoryPort {

    private final AssetMongoRepository repository;

    public AssetRepositoryAdapter(AssetMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Asset save(Asset asset) {
        AssetDocument doc = AssetMapper.toDocument(asset);
        AssetDocument saved = repository.save(doc);
        return AssetMapper.toDomain(saved);
    }

    @Override
    public Optional<Asset> findByTicker(String ticker) {
        if (ticker == null) return Optional.empty();
        return repository.findById(ticker.trim().toUpperCase())
                .map(AssetMapper::toDomain);
    }

    @Override
    public List<Asset> findAll() {
        return repository.findAll().stream()
                .map(AssetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByTicker(String ticker) {
        if (ticker != null) {
            repository.deleteById(ticker.trim().toUpperCase());
        }
    }

    @Override
    public boolean existsByTicker(String ticker) {
        if (ticker == null) return false;
        return repository.existsById(ticker.trim().toUpperCase());
    }
}
