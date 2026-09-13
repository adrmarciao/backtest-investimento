package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.Asset;
import com.backtest.backend.domain.port.in.RegisterAssetPort;
import com.backtest.backend.domain.port.out.AssetRepositoryPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RegisterAssetUseCase implements RegisterAssetPort {

    private final AssetRepositoryPort assetRepositoryPort;

    public RegisterAssetUseCase(AssetRepositoryPort assetRepositoryPort) {
        this.assetRepositoryPort = assetRepositoryPort;
    }

    @Override
    public Asset registerAsset(Asset asset) {
        if (asset == null || asset.getTicker() == null || asset.getTicker().isBlank()) {
            throw new IllegalArgumentException("Ticker é obrigatório");
        }
        if (asset.getValorAporte() == null || asset.getValorAporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de aporte deve ser maior que zero");
        }
        if (asset.getPeriodicidade() == null) {
            throw new IllegalArgumentException("Periodicidade é obrigatória");
        }
        return assetRepositoryPort.save(asset);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepositoryPort.findAll();
    }

    @Override
    public Optional<Asset> getAssetByTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            return Optional.empty();
        }
        return assetRepositoryPort.findByTicker(ticker.trim().toUpperCase());
    }

    @Override
    public void deleteAsset(String ticker) {
        if (ticker != null && !ticker.isBlank()) {
            assetRepositoryPort.deleteByTicker(ticker.trim().toUpperCase());
        }
    }
}
