package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.Asset;
import java.util.List;
import java.util.Optional;

public interface AssetRepositoryPort {
    Asset save(Asset asset);
    Optional<Asset> findByTicker(String ticker);
    List<Asset> findAll();
    void deleteByTicker(String ticker);
    boolean existsByTicker(String ticker);
}
