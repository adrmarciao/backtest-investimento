package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.Asset;
import java.util.List;
import java.util.Optional;

public interface RegisterAssetPort {
    Asset registerAsset(Asset asset);
    List<Asset> getAllAssets();
    Optional<Asset> getAssetByTicker(String ticker);
    void deleteAsset(String ticker);
}
