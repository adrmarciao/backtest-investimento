package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.PurchasePlanConfig;
import com.backtest.backend.domain.port.in.ManagePurchasePlanConfigPort;
import com.backtest.backend.domain.port.out.PurchasePlanConfigRepositoryPort;

public class ManagePurchasePlanConfigUseCase implements ManagePurchasePlanConfigPort {

    private final PurchasePlanConfigRepositoryPort repositoryPort;

    public ManagePurchasePlanConfigUseCase(PurchasePlanConfigRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PurchasePlanConfig getConfig() {
        return repositoryPort.findDefault().orElseGet(PurchasePlanConfig::new);
    }

    @Override
    public PurchasePlanConfig saveConfig(PurchasePlanConfig config) {
        if (config == null) {
            config = new PurchasePlanConfig();
        }
        if (config.getId() == null || config.getId().isBlank()) {
            config.setId("default");
        }
        return repositoryPort.save(config);
    }
}
