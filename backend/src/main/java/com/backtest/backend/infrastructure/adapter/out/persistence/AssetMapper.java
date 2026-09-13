package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.Asset;

public class AssetMapper {

    public static AssetDocument toDocument(Asset asset) {
        if (asset == null) return null;
        return new AssetDocument(asset.getTicker(), asset.getValorAporte(), asset.getPeriodicidade());
    }

    public static Asset toDomain(AssetDocument doc) {
        if (doc == null) return null;
        return new Asset(doc.getTicker(), doc.getValorAporte(), doc.getPeriodicidade());
    }
}
