package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.PurchasePlanAsset;
import java.util.List;
import java.util.Optional;

public interface PurchasePlanAssetRepositoryPort {
    PurchasePlanAsset save(PurchasePlanAsset asset);
    List<PurchasePlanAsset> saveAll(List<PurchasePlanAsset> assets);
    List<PurchasePlanAsset> findAll();
    Optional<PurchasePlanAsset> findByTicker(String ticker);
    void deleteByTicker(String ticker);
    void deleteAll();
}
