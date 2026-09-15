package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.PurchasePlanAsset;
import com.backtest.backend.domain.port.out.PurchasePlanAssetRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PurchasePlanAssetRepositoryAdapter implements PurchasePlanAssetRepositoryPort {

    private final PurchasePlanAssetMongoRepository repository;

    public PurchasePlanAssetRepositoryAdapter(PurchasePlanAssetMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PurchasePlanAsset save(PurchasePlanAsset asset) {
        PurchasePlanAssetDocument doc = PurchasePlanAssetMapper.toDocument(asset);
        PurchasePlanAssetDocument saved = repository.save(doc);
        return PurchasePlanAssetMapper.toDomain(saved);
    }

    @Override
    public List<PurchasePlanAsset> saveAll(List<PurchasePlanAsset> assets) {
        if (assets == null) return List.of();
        List<PurchasePlanAssetDocument> docs = assets.stream()
                .map(PurchasePlanAssetMapper::toDocument)
                .collect(Collectors.toList());
        return repository.saveAll(docs).stream()
                .map(PurchasePlanAssetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchasePlanAsset> findAll() {
        return repository.findAllByOrderByOrdemAsc().stream()
                .map(PurchasePlanAssetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PurchasePlanAsset> findByTicker(String ticker) {
        if (ticker == null) return Optional.empty();
        return repository.findByTickerIgnoreCase(ticker.trim())
                .map(PurchasePlanAssetMapper::toDomain);
    }

    @Override
    public void deleteByTicker(String ticker) {
        if (ticker != null) {
            repository.deleteByTickerIgnoreCase(ticker.trim());
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
